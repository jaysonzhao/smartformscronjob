package com.gzsolartech.schedule.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzsolartech.bpmportal.entity.AacEmployee;
import com.gzsolartech.bpmportal.service.AACEmployeeService;
import com.gzsolartech.bpmportal.service.KronosService;
import com.gzsolartech.smartforms.constant.bpm.BpmMailSendType;
import com.gzsolartech.smartforms.constant.bpm.BpmRoutingType;
import com.gzsolartech.smartforms.entity.DatContentTemplate;
import com.gzsolartech.smartforms.entity.OrgEmployee;
import com.gzsolartech.smartforms.entity.bpm.BpmActivityMeta;
import com.gzsolartech.smartforms.entity.bpm.BpmEmailNotificationRecord;
import com.gzsolartech.smartforms.entity.bpm.BpmGlobalConfig;
import com.gzsolartech.smartforms.entity.bpm.BpmTaskInfo;
import com.gzsolartech.smartforms.service.BaseDataService;
import com.gzsolartech.smartforms.service.DatContentTemplateService;
import com.gzsolartech.smartforms.service.DatDocumentService;
import com.gzsolartech.smartforms.service.OrgEmployeeService;
import com.gzsolartech.smartforms.service.SysConfigurationService;
import com.gzsolartech.smartforms.service.bpm.BpmActivityMetaService;
import com.gzsolartech.smartforms.service.bpm.BpmEmailNotificationRecordService;
import com.gzsolartech.smartforms.service.bpm.BpmGlobalConfigService;
import com.gzsolartech.smartforms.service.bpm.BpmTaskInfoService;
import com.gzsolartech.smartforms.service.bpm.router.TransferTaskRouterService;

/**
 * 
 * @ClassName: BpmTaskInfoHastenService
 * @Description: bpm 待办自动催办的调度任务
 * @author wwd
 * @date 2017年5月16日 上午9:22:06
 *
 */
@Service
public class BpmTaskInfoHastenService extends BaseDataService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BpmTaskInfoHastenService.class);
	@Autowired
	private BpmTaskInfoService bpmTaskInfoService;
	@Autowired
	private DatDocumentService datDocumentService;
	@Autowired
	private SysConfigurationService sysConfigurationService;
	@Autowired
	private DatContentTemplateService datContentTemplateService;
	@Autowired
	private BpmGlobalConfigService bpmGlobalConfigService;
	@Autowired
	private BpmActivityMetaService bpmActivityMetaService;
	@Autowired
	private BpmEmailNotificationRecordService bpmEmailNotificationRecordService;
	@Autowired
	private AACEmployeeService aacEmployeeService;
	@Autowired
   private OrgEmployeeService orgEmployeeService;
	@Autowired
	private SendEmailService sendEmailService;
	@Autowired
	private TransferTaskRouterService transferTaskRouterService;
	@Autowired
	private KronosService kronosService;
	/**
	 * @throws Exception 
	 * 自动催办
	 *
	 * @return void 返回类型
	 * @throws
	 */
	public void execute() throws Exception {
		 LOGGER.debug("催办调度任务开始执行");
		// JSONObject j=kronosService.getAllLeave("60001347","2017-05-19");
		 //System.out.println(j.toString()); 
		
		 // 获取所的的待办
		List<BpmTaskInfo> bpmTaskInfos = bpmTaskInfoService
				.getAllReceivedTask();
		// 遍历待办信息
		for (BpmTaskInfo bpmTaskInfo : bpmTaskInfos) {
			try {
				// 流程快照
				String snapshotBpdId = bpmTaskInfo.getSnapshotBpdId();
				// 流程图环节ID
				String nodeId = bpmTaskInfo.getNodeId();
				// 获取当前待办所在的环节信息
				BpmActivityMeta bpmActivityMeta = bpmActivityMetaService
						.getBpmActivityMeta(nodeId, snapshotBpdId);
				// 环节信息不为空
				if (bpmActivityMeta != null) {
					// 待办创建时间距离现在多少分钟
					int gapMiute = currentTimeGap(bpmTaskInfo.getCreateTime());
					// 重复初始时间
					Integer loopStartTimeSpan = bpmActivityMeta	.getLoopStartTimeSpan();
					if (loopStartTimeSpan != null) {
						// 重复初始时间 转化为分钟
						loopStartTimeSpan = toMinute(bpmActivityMeta.getLoopStartTimeUnit(),loopStartTimeSpan);
						OrgEmployee employee = gdao.findById(OrgEmployee.class,bpmTaskInfo.getAssignedTo());
						//待办创建时间格式化
						String createTime=timestampFormat(bpmTaskInfo.getCreateTime());
						 String nowTime=timestampFormat(new Timestamp(System.currentTimeMillis())); 
						//调用HR接口 获取可申请假期的小时
						 JSONObject kron= kronosService.checkDataRound(employee.getJobNumber(), "123", createTime.substring(0, 10), createTime.substring(11, 16), nowTime.substring(0, 10), nowTime.substring(11, 16));
						 int amountInTime=kron.getJSONObject("CNLeaveRequest").getInt("AmountInTime");
						LOGGER.debug("调用HR接口 获取可申请假期的小时:"+amountInTime);
						 amountInTime=amountInTime*60;
						// System.out.println("创建时间:"+createTime);
						// System.out.println("从HR获取分钟:"+amountInTime);
						// System.out.println("创建到现在分钟:"+gapMiute);
						 //LOGGER.debug("totalMiute="+gapMiute+"-"+amountInTime+"="+(gapMiute - loopStartTimeSpan));
						 gapMiute=gapMiute-amountInTime;
						 //System.out.println("减去HR后分钟:"+gapMiute);
						int totalMiute=gapMiute - loopStartTimeSpan ;
						//LOGGER.debug("totalMiute="+gapMiute+"-"+loopStartTimeSpan);
						if (totalMiute>= 0) {
							remind(bpmTaskInfo, bpmActivityMeta);
						}
					}
                  //最迟延迟事件
					Integer overTimeDeadline=bpmActivityMeta.getOverTimeDeadline();
					if(overTimeDeadline!=null){
						overTimeDeadline = toMinute(
							bpmActivityMeta.getOverTimeDeadlineUnit(),overTimeDeadline);
						if(gapMiute-overTimeDeadline>0){
							System.out.println("执行转签===========");
							//transfer(bpmTaskInfo);
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error("自动催办出现异常", e);
			}
		}
		 LOGGER.debug("催办调度任务结束执行");
	}

	/**
	 * 超时未处理转签领导处理
	 *@param bpmTaskInfo
	 * @return void    返回类型 
	 * @throws
	 */
	public void transfer(BpmTaskInfo bpmTaskInfo){
	    OrgEmployee employee = gdao.findById(OrgEmployee.class,bpmTaskInfo.getAssignedTo());
		if(employee!=null){
	    OrgEmployee leader=getLeaderShip(employee.getEmpNum());
	    if(leader!=null){
	    Map<String,Object> data=datDocumentService.getDocumentById(bpmTaskInfo.getDocumentId());
        BpmGlobalConfig gcfg = bpmGlobalConfigService.getFirstActConfig();
		String targetUser = leader.getNickName()+"("+leader.getEmpNum()+")";
		String taskId = bpmTaskInfo.getTaskId();
		String documentId =bpmTaskInfo.getDocumentId();
		String appId = data.get("__appid")+"";
		String note =employee.getNickName()+"待办超时未处理转签给"+targetUser;
		String fromUserId =employee.getEmpNum();
		String nodeId = bpmTaskInfo.getNodeId();
		String instanceId =bpmTaskInfo.getInstanceId();
		// 调用bpm转签的接口进行转签
	/*	transferTaskRouterService.executeAuthorizeTransfer(request, gcfg,
				targetUser, taskId, documentId, appId, note, fromUserId, 
				BpmRoutingType.OVERTIME_TRANSFER);*/
		}
		}
	}
	/**
	 * 指定单位下的时间转换成时间
	 *
	 * @param unit
	 * @param time
	 * @return
	 * @return void 返回类型
	 * @throws
	 */
	public int toMinute(String unit, int time) {
		int minutes = 0;
		if ("day".equals(unit)) {
			minutes = 24 * 60 * time;
		} else if ("hour".equals(unit)) {
			minutes = 60 * time;
		} else {
			minutes = time;
		}
		return minutes;
	}

	/**
	 * 指定时间戳与当前时间相差多少分钟
	 *
	 * @param createTime
	 * @return
	 * @return void 返回类型
	 * @throws
	 */
	public int currentTimeGap(Timestamp createTime) {
		long create = createTime.getTime();
		long difference = (System.currentTimeMillis() - create) / (1000 * 60);
		return new Long(difference).intValue();
	}

	public void remind(BpmTaskInfo bpmTaskInfo, BpmActivityMeta bpmActivityMeta) {
		 LOGGER.debug("邮件通知:" + bpmTaskInfo.getTaskName());
		String hql = "from BpmEmailNotificationRecord where documentId=? and metaId=?  and assignTo=? order by assignTo desc";
		String documentId = bpmTaskInfo.getDocumentId();
		String assignTo = bpmTaskInfo.getAssignedTo();
		List<BpmEmailNotificationRecord> bpmEmailRecords = gdao.queryHQL(hql, documentId,
				bpmActivityMeta.getActivityId(), assignTo);
		if (!bpmEmailRecords.isEmpty()) {
			BpmEmailNotificationRecord bpmEmailRecord = bpmEmailRecords.get(0);
			Integer loopTimeSpan = bpmActivityMeta.getLoopTimeSpan();
			String overtimeNotifyIsLoop = bpmActivityMeta
					.getOvertimeNotifyIsLoop();
			String loopTimeUnit = bpmActivityMeta.getLoopTimeUnit();
			Timestamp createTime = bpmEmailRecord.getCreateTime();
			// 重复通知
			if ("yes".equals(overtimeNotifyIsLoop)) {
				// 重复初始时间 转化为分钟
				loopTimeSpan = toMinute(loopTimeUnit, loopTimeSpan);
				// 上次通知时间距离现在多少分钟
				int gapMiute = currentTimeGap(createTime);
				if ((gapMiute - loopTimeSpan) >= 0) {
					loopRemind(bpmTaskInfo, bpmActivityMeta, bpmEmailRecord);
				}
			}
		} else {
			// 第一次邮件通知
			firstRemind(bpmTaskInfo, bpmActivityMeta);
		}
	}

	/**
	 * 第一次邮件通
	 *
	 * @param bpmTaskInfo
	 * @param bpmActivityMeta
	 * @return void 返回类型
	 * @throws
	 */
	public void firstRemind(BpmTaskInfo bpmTaskInfo,
			BpmActivityMeta bpmActivityMeta) {
		OrgEmployee employee = gdao.findById(OrgEmployee.class,
				bpmTaskInfo.getAssignedTo());
		if (employee != null) {
			String email = employee.getEmail();
			Map<String, Object> data = datDocumentService
					.getDocumentById(bpmTaskInfo.getDocumentId());
			data.put("_taskId", bpmTaskInfo.getTaskId());
			 LOGGER.debug("第一次邮件提醒: " + employee.getNickName() + " email="
					+ email);
			DatContentTemplate datContentTemplate = datContentTemplateService
					.load(bpmActivityMeta.getOvertimeTemplateId());
			String content = "有待办信息需要您及时处理:<br>";
			String title = "待办信息提醒";
			if (datContentTemplate != null) {
				title = datContentTemplate.getTitle();
				content = datContentTemplate.getTextContent();
				if (StringUtils.isNotBlank(title)) {
					title = pasePlaceholder(title, data);
				}
				if (StringUtils.isNotBlank(content)) {
					content = pasePlaceholder(content, data);
				}
			}
			// 发送邮件
		sendEmailService.sendEmail(email, title, content, null);
			seveEmailRecord(email, title, content, null, bpmTaskInfo,
					bpmActivityMeta);
		}
	}

	/**
	 * 重复通知
	 *
	 * @param bpmTaskInfo
	 * @param bpmActivityMeta
	 * @return void 返回类型
	 * @throws
	 */
	public void loopRemind(BpmTaskInfo bpmTaskInfo,
			BpmActivityMeta bpmActivityMeta, BpmEmailNotificationRecord bpmEmailRecord) {
		OrgEmployee employee = gdao.findById(OrgEmployee.class,
				bpmTaskInfo.getAssignedTo());
		if (employee != null) {
			String email = employee.getEmail();
			Map<String, Object> data = datDocumentService
					.getDocumentById(bpmTaskInfo.getDocumentId());
			data.put("_taskId", bpmTaskInfo.getTaskId());
			 LOGGER.debug("重复邮件提醒: " + employee.getNickName() + " email="
					+ email);
			DatContentTemplate datContentTemplate = datContentTemplateService
					.load(bpmActivityMeta.getOvertimeLoopTplId());
			String content = "有待办信息需要您及时处理:<br>";
			String title = "待办信息提醒";
			if (datContentTemplate != null) {
				title = datContentTemplate.getTitle();
				content = datContentTemplate.getTextContent();
				if (StringUtils.isNotBlank(title)) {
					title = pasePlaceholder(title, data);
				}
				if (StringUtils.isNotBlank(content)) {
					content = pasePlaceholder(content, data);
				}
			}
			String copyToEmpId = bpmEmailRecord.getCopyTo();
			if (StringUtils.isBlank(copyToEmpId)) {
				copyToEmpId = bpmTaskInfo.getAssignedTo();
			}
			OrgEmployee tmp = getLeaderShip(copyToEmpId);
			String copyTo = tmp.getEmail();
			copyToEmpId = tmp.getEmpNum();
			// 发送邮件
			sendEmailService.sendEmail(email, title, content, copyTo);
			seveEmailRecord(email, title, content, copyToEmpId, bpmTaskInfo,
				bpmActivityMeta);
		}
	}
    
	/**
	 * 获取直属领导
	 *@param empId
	 *@return
	 * @return void    返回类型 
	 * @throws
	 */
	public OrgEmployee getLeaderShip(String empId) {
		
		OrgEmployee directorEmployee = new OrgEmployee();
		OrgEmployee employee = gdao.findById(OrgEmployee.class, empId);
		if (employee != null) {
		
			String hql = "from AacEmployee where lower(ad)=?";
			List<AacEmployee> aacEmployees = gdao.queryHQL(hql, employee
					.getEmpName().toLowerCase());
			System.out.println("取得的直属aac："+aacEmployees);
			if (!aacEmployees.isEmpty()) {
				AacEmployee aacEmployee = aacEmployees.get(0);
				String director=aacEmployeeService.getDirectLeader(aacEmployee);
			    if(StringUtils.isNotBlank(director)){
			    	LOGGER.debug("直属领导域账号"+director);
			    	List<AacEmployee> aacEmpExist = gdao.queryHQL(hql,director.toLowerCase());
			    	   if(!aacEmpExist.isEmpty()){
			    		   String ad=aacEmpExist.get(0).getAd();
			    		 OrgEmployee tmp=orgEmployeeService.getUserByCN(ad);
			    		 if(tmp!=null){
			    			 directorEmployee=tmp;
			    			 System.out.println(employee.getNickName()+"的直属领导"+tmp.getNickName()+"  "+tmp.getEmail());
			    		 }
			    	   }
			    }
			}
			 LOGGER.debug(employee.getNickName()+"获取上级领导:"+directorEmployee.getNickName()+"  "+directorEmployee.getEmail());
		}
		return directorEmployee;
	}

	/**
	 * 内容替换
	 *
	 * @param conent
	 * @param data
	 * @return
	 * @return void 返回类型
	 * @throws
	 */
	public String pasePlaceholder(String conent, Map<String, Object> data) {
		if (StringUtils.isNotBlank(conent) && data != null) {
			String regEx = "\\{[^}]*\\}";
			Pattern pat = Pattern.compile(regEx);
			Matcher mat = pat.matcher(conent);
			while (mat.find()) {
				String tmp = mat.group(0);
				tmp = tmp.replaceAll("\\{", "");
				tmp = tmp.replaceAll("\\}", "");
				String value = data.get(tmp) + "";
				conent = conent.replaceAll("\\{" + tmp + "\\}", value);
			}
		}
		return conent;
	}

	

	/**
	 * 记录邮件发送
	 *
	 * @param recipient
	 * @param title
	 * @param content
	 * @param copyTo
	 * @param bpmTaskInfo
	 * @param bpmActivityMeta
	 * @return void 返回类型
	 * @throws
	 */
	public void seveEmailRecord(String recipient, String title, String content,
			String copyTo, BpmTaskInfo bpmTaskInfo,
			BpmActivityMeta bpmActivityMeta) {
		if (StringUtils.isNotBlank(recipient)) {
			BpmEmailNotificationRecord bpmEmailRecord = new BpmEmailNotificationRecord();
			bpmEmailRecord.setContent(content);
			bpmEmailRecord.setTitle(title);
			bpmEmailRecord.setRecipients(recipient);
			bpmEmailRecord.setCopyTo(copyTo);
			bpmEmailRecord.setDocumentId(bpmTaskInfo.getDocumentId());
			bpmEmailRecord.setAssignTo(bpmTaskInfo.getAssignedTo());
			bpmEmailRecord.setMetaId(bpmActivityMeta.getActivityId());
			bpmEmailRecord.setSendType(BpmMailSendType.ACTIVITY_OVERTIME);
			bpmEmailRecord.setBpmTaskInfoId(bpmTaskInfo.getInfoId());
			bpmEmailNotificationRecordService.create(bpmEmailRecord);
		}
	}
	
	/**
	 * 时间戳格式化
	 * @param timestamp
	 * @return
	 */
		public String timestampFormat(Timestamp timestamp){
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
		   return sdf.format(timestamp); 
		}
		

}
