package com.gzsolartech.schedule.quartz.task;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.gzsolartech.schedule.constant.BpmApiPathPrefix;
import com.gzsolartech.schedule.utils.HttpLightUtils;
import com.gzsolartech.smartforms.constant.EntitySwitchSignal;
import com.gzsolartech.smartforms.constant.bpm.BpmActivityType;
import com.gzsolartech.smartforms.constant.bpm.BpmApiEntityStatus;
import com.gzsolartech.smartforms.constant.bpm.RollbackGroupStrategy;
import com.gzsolartech.smartforms.entity.DatDocument;
import com.gzsolartech.smartforms.entity.OrgEmployee;
import com.gzsolartech.smartforms.entity.bpm.BpmActivityMeta;
import com.gzsolartech.smartforms.entity.bpm.BpmGlobalConfig;
import com.gzsolartech.smartforms.entity.bpm.BpmTaskInfo;
import com.gzsolartech.smartforms.exceptions.SmartformsException;
import com.gzsolartech.smartforms.exentity.HttpReturnStatus;
import com.gzsolartech.smartforms.service.DatDocumentService;
import com.gzsolartech.smartforms.service.OrgEmployeeService;
import com.gzsolartech.smartforms.service.bpm.BpmActivityMetaService;
import com.gzsolartech.smartforms.service.bpm.BpmGlobalConfigService;
import com.gzsolartech.smartforms.service.bpm.BpmTaskInfoService;
import com.gzsolartech.smartforms.utils.UserDialogTagUtils;
import com.gzsolartech.smartforms.utils.bpm.BpmClientUtils;
import com.gzsolartech.smartforms.utils.bpm.BpmTaskUtils;

/**
 * 定时自动提交审批任务类
 * @author sujialin
 *
 */
@Component
public class BpmAutoCommitTask extends BaseTask {
	private static final Logger LOG = LoggerFactory
			.getLogger(BpmAutoCommitTask.class);

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext=applicationContext;
	}

	@Override
	public void run(String jobId) {
		LocalDateTime ldtnow=LocalDateTime.now();
		BpmGlobalConfigService bpmGlobalConfigService=(BpmGlobalConfigService)applicationContext
				.getBean(BpmGlobalConfigService.class);
		BpmGlobalConfig bpmcfg=bpmGlobalConfigService.getFirstActConfig();
		BpmTaskInfoService bpmTaskInfoService=(BpmTaskInfoService)applicationContext
				.getBean(BpmTaskInfoService.class);
		BpmActivityMetaService bpmActivityMetaService=(BpmActivityMetaService)applicationContext
				.getBean(BpmActivityMetaService.class);
		DatDocumentService datDocumentService=(DatDocumentService)applicationContext
				.getBean(DatDocumentService.class);
		//获取BPM管理员的待办
		OrgEmployeeService orgEmployeeService=(OrgEmployeeService)applicationContext
				.getBean(OrgEmployeeService.class);
		OrgEmployee orgadminEmp=orgEmployeeService.getEmpByName(bpmcfg.getBpmAdminName());
		if (orgadminEmp==null || StringUtils.isBlank(orgadminEmp.getEmpNum())) {
			LOG.error("找不到BPM管理员帐号，无法进行待办自动提交操作！");
			return;
		}
		List<BpmTaskInfo> adminTodoList=bpmTaskInfoService.getUserAllTaskInfo(orgadminEmp.getEmpNum());
		if (!CollectionUtils.isEmpty(adminTodoList)) {
			HttpLightUtils httpUtils=new HttpLightUtils();
			CloseableHttpClient httpClient=httpUtils.getClient();
			String apiHost=bpmcfg.getBpmApiHost();
			if (StringUtils.isBlank(apiHost)) {
				LOG.error("无法确定smartformAPI平台的资源位置，请设置表单平台API接口地址！");
				return;
			}
			apiHost = apiHost.endsWith("/") ? apiHost : apiHost+"/";
			//登录获取Token
			String loginUrl=apiHost+MessageFormat.format(BpmApiPathPrefix.BPM_LOGIN, 
					bpmcfg.getBpmAdminName(), bpmcfg.getBpmAdminPsw());
			HttpReturnStatus retstatus=httpUtils.doPost(httpClient, loginUrl, new HashMap<>());
			String token="";
			if (HttpStatus.SC_OK!=retstatus.getCode()) {
				LOG.error("BPM管理员登录失败，无法进行待办自动提交操作！详细信息："+retstatus.getMsg());
				return;
			} else {
				try {
					JSONObject jsoMsg=new JSONObject(retstatus.getMsg());
					JSONObject jsoMsg2=jsoMsg.optJSONObject("msg");
					if (jsoMsg2==null) {
						LOG.error("登录返回的数据格式有误！returnMsg="+retstatus.getMsg());
						return;
					}
					token=jsoMsg2.optString("token", "");
				} catch (Exception ex) {
					LOG.error("登录返回的数据格式有误！returnMsg="+retstatus.getMsg(), ex);
				}
			}
			if (StringUtils.isBlank(token)) {
				LOG.error("身份令牌是空的，无法提交审批任务！");
				return;
			}
			
			//使用BPM管理员身份进行登录，获取Cookie信息
			BpmClientUtils bpmUtils=new BpmClientUtils(bpmcfg, true);
			List<Cookie> cookies=new ArrayList<Cookie>();
			HttpReturnStatus bpmLoginStatus=bpmUtils.doLogin(bpmcfg.getBpmAdminName(), 
					bpmcfg.getBpmAdminPsw(), cookies);
			bpmUtils.closeClient();
			if (CollectionUtils.isEmpty(cookies)) {
				LOG.error("BPM登录失败，没有获取到身份Cookie！loginMsg="+bpmLoginStatus.getMsg());
				return;
			}
			BpmTaskUtils taskUtils=new BpmTaskUtils(bpmcfg, true);
			
			for (BpmTaskInfo todoTask : adminTodoList) {
				//判断待办任务是否允许自动提交，若环节配置中设置为允许自动提交，
				//则调用BPM API的提交接口进行任务提交。
				BpmActivityMeta taskActyMeta=null;
				try {
					taskActyMeta=bpmActivityMetaService.getActivityByTaskId(
							todoTask.getTaskId(), 
							todoTask.getDocumentId());
				} catch (SmartformsException e) {
					LOG.error("获取任务所属环节元数据时发生异常！", e);
					taskActyMeta=null;
				}
				if (taskActyMeta!=null) {
					String autoCommit=taskActyMeta.getAutoCommit();
					if (EntitySwitchSignal.ON.equals(autoCommit)) {
						//获取BPM任务详细判断，判断任务处理人是否BPM管理员
						//如果不是的话，则跳过任务，不进行提交
						HttpReturnStatus taskHttpStatus=taskUtils.getTaskDetails(cookies.get(0), 
								todoTask.getTaskId());
						if (BpmClientUtils.isErrorResult(taskHttpStatus)) {
							LOG.error("获取任务详细信息失败！taskId="+todoTask.getTaskId()+
									", returnMsg="+taskHttpStatus.getMsg());
							continue;
						}
						JSONObject jsoTaskMsg=new JSONObject(taskHttpStatus.getMsg());
						JSONObject jsoTaskData=jsoTaskMsg.getJSONObject("data");
						String assignedTo=jsoTaskData.optString("assignedTo", "");
						String taskState=jsoTaskData.optString("state", "");
						String taskStatus=jsoTaskData.optString("status", "");
						if (!assignedTo.equals(bpmcfg.getBpmAdminName())) {
							LOG.warn("任务处理人不是BPM管理员，无法自动提交审批任务！"
									+ "taskId="+todoTask.getTaskId());
							continue;
						}
						if (!BpmApiEntityStatus.TASK_DOING_STATUS.equals(taskStatus) ||
								!BpmApiEntityStatus.TASK_CLAIMED_STATE.equals(taskState)) {
							LOG.warn("任务不是活动状态，无法自动提交审批任务！"
									+ "taskId="+todoTask.getTaskId()+", "
									+ "taskState="+taskState+", "
									+ "taskStatus="+taskStatus);
							continue;
						}
						
						//判断是否要延迟执行提交
						String delayCls=taskActyMeta.getCommitDelayCls();
						if (StringUtils.isNotBlank(delayCls)) {
							int delayMins=0;
							try {
								Class<?> clz = Class.forName(delayCls);
        						Object obj = clz.newInstance();
        						 // 获取方法
        					    Method md = obj.getClass().getDeclaredMethod(
        								"execute", String.class, String.class);
        						 // 调用方法
        					    Integer result=(Integer)md.invoke(obj, todoTask.getDocumentId(), 
        					    		todoTask.getTaskId());
        					    delayMins=result.intValue();
							} catch (Exception ex) {
								delayMins=0;
								LOG.error("提交延迟执行策略异常！", ex);
							}
							
							if (delayMins>0) {
								//根据任务ID获取待办任务列表，然后获取待办任务创建时间
								List<BpmTaskInfo> tasks=bpmTaskInfoService.getTaskInfoByTaskId(todoTask.getTaskId());
								if (!CollectionUtils.isEmpty(tasks)) {
									BpmTaskInfo task1=tasks.get(0);
									//计算任务创建时间+延迟执行时间
									Timestamp tscreate=task1.getCreateTime();
									LocalDateTime ldtCreated=LocalDateTime.ofInstant(
											Instant.ofEpochMilli(tscreate.getTime()), ZoneId.of("Asia/Shanghai"));
									LocalDateTime newLdtTime=ldtCreated.plusMinutes(delayMins);
									if (ldtnow.isBefore(newLdtTime)) {
										//当前时间在延迟执行的时间之前，还不能提交待办，等待下一次
										continue;
									}
								}
							}
						}
						
						//进行自动提交
						String url=apiHost+BpmApiPathPrefix.BPM_SUBMIT_TASK;
						Map<String, Object> params=new HashMap<>();
						params.put("token", token);
						params.put("documentId", todoTask.getDocumentId());
						//获取应用库ID
						String appId="";
						DatDocument datdoc=datDocumentService.loadWithDatApp(todoTask.getDocumentId());
						if (datdoc!=null && StringUtils.isNotBlank(datdoc.getDocumentId())) {
							appId=datdoc.getDatApplication().getAppId();
						}
						params.put("appId", appId);
						params.put("taskId", todoTask.getTaskId());
						params.put("note", "BPM管理员自动提交");
						params.put("rollbackGroupType", RollbackGroupStrategy.ALL);
						params.put("params", "{}");
						//获取下一环节和下一环节处理人，把这些信息填入提交参数中
						JSONObject jsoNextOwners=new JSONObject();
						JSONObject jsoNextNodes=new JSONObject();
						JSONObject jsoNextOwnerNames=new JSONObject();
						List<BpmActivityMeta> nextActies=bpmActivityMetaService
								.getNextHumanActivity(taskActyMeta.getActivityBpdId(), 
										todoTask.getSnapshotBpdId(), todoTask.getDocumentId());
						if (!CollectionUtils.isEmpty(nextActies)) {
							//先按照序号，再按照名称，对环节元数据从小到大进行排序
							nextActies=nextActies.stream().sorted((e1, e2)->{
								Integer it1=e1.getSortNum();
								it1 = (it1==null) ? 0 : it1.intValue();
								Integer it2=e2.getSortNum();
								it2 = (it2==null) ? 0 : it2.intValue();
								if (it1.equals(it2)) {
									return e1.getActivityId().compareTo(e2.getActivityId());
								} else {
									return it1.compareTo(it2);
								}
							}).collect(Collectors.toList());
							
							int sortNum=0;
							for (BpmActivityMeta nextActy : nextActies) {
								//判断是否会签环节，会签环节不支持自动提交，所以不会为会签环节赋值处理人
								if (BpmActivityType.ALONE_SIGN.equals(nextActy.getHandleSignType())) {
									String nodeIdKey="__nextNodeId_"+sortNum;
									jsoNextNodes.put(nodeIdKey, nextActy.getActivityBpdId());
									String ownerKey="__nextOwner_"+sortNum+"_num";
									String ownerNameKey="__nextOwner_"+sortNum;
									BpmActivityMeta actyMeta=bpmActivityMetaService.getNextToPerson(
											nextActy.getActivityBpdId(), 
											todoTask.getSnapshotBpdId(), 
											todoTask.getDocumentId());
									String nextOwnersStr=actyMeta.getDefaultOwnersName();
									List<OrgEmployee> nextOwnerEmps=UserDialogTagUtils
											.parseUserSelects(nextOwnersStr);
									if (CollectionUtils.isEmpty(nextOwnerEmps)) {
										jsoNextOwners.put(ownerKey, "");
										jsoNextOwnerNames.put(ownerNameKey, "");
									} else {
										jsoNextOwners.put(ownerKey, nextOwnersStr);
										String tmpOwnerNames="";
										for (OrgEmployee orgemp : nextOwnerEmps) {
											tmpOwnerNames+=orgemp.getNickName()+";";
										}
										jsoNextOwnerNames.put(ownerNameKey, tmpOwnerNames);
									}
									sortNum++;
								}
							}
						}
						params.put("nextOwners", jsoNextOwners.toString());
						params.put("nextNodes", jsoNextNodes.toString());
						params.put("nextOwnerNames", jsoNextOwnerNames.toString());
						//自动提交标志位
						params.put("autoCommit", "1");
						
						HttpReturnStatus commitStatus=httpUtils.doPost(httpClient, url, params);
						if (HttpStatus.SC_OK!=commitStatus.getCode()) {
							//提交失败
							LOG.error("BPM管理员自动提交审批任务失败！taskId="+todoTask.getTaskId()+
									", 详细信息："+commitStatus.getMsg());
						}
					} // end if (EntitySwitchSignal.ON.equals(autoCommit))
				} // end if (taskActyMeta!=null)
			} // end for (BpmTaskInfo todoTask : adminTodoList) 
			
			taskUtils.closeClient();
			try {
				httpClient.close();
			} catch (IOException e) {
				LOG.error("关闭HttpClient时发生异常！", e);
			}
		}
	}

}
