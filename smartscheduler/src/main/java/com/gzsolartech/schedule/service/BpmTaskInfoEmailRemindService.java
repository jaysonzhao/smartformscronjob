package com.gzsolartech.schedule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzsolartech.bpmportal.util.email.EmailNotificationUtil;
import com.gzsolartech.smartforms.constant.SysConfigurationTypeName;
import com.gzsolartech.smartforms.entity.DatContentTemplate;
import com.gzsolartech.smartforms.entity.OrgEmployee;
import com.gzsolartech.smartforms.entity.bpm.BpmTaskInfo;
import com.gzsolartech.smartforms.service.BaseDataService;
import com.gzsolartech.smartforms.service.DatContentTemplateService;
import com.gzsolartech.smartforms.service.DatDocumentService;
import com.gzsolartech.smartforms.service.SysConfigurationService;
import com.gzsolartech.smartforms.service.bpm.BpmGlobalConfigService;
import com.gzsolartech.smartforms.service.bpm.BpmTaskInfoService;
/**
 * 
* @ClassName: BpmTaskInfoEmailRemindService 
* @Description: 邮件提醒调度任务
* @author wwd 
* @date 2017年5月15日 下午1:30:49 
*
 */
@Service
public class BpmTaskInfoEmailRemindService extends BaseDataService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BpmTaskInfoEmailRemindService.class);
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
	private SenEmailService senEmailService;
	/**
	 *执行邮件提醒的操作
	 * @return void    返回类型 
	 * @throws
	 */
	public void execute() {
		//获取所的的待办
		List<BpmTaskInfo> bpmTaskInfos = bpmTaskInfoService.getAllReceivedTask();
		//取得的待办信息按照当前处理人进行分组
		Map<String, List<BpmTaskInfo>> data = new HashMap<String, List<BpmTaskInfo>>();
		for (int i = 0; i < bpmTaskInfos.size(); i++) {
			BpmTaskInfo bpmTaskInfo = (BpmTaskInfo) bpmTaskInfos.get(i);
			String assignedTo = bpmTaskInfo.getAssignedTo();
			if (data.containsKey(assignedTo)) {
				data.get(assignedTo).add(bpmTaskInfo);
			} else {
				List<BpmTaskInfo> tmp = new ArrayList<BpmTaskInfo>();
				tmp.add(bpmTaskInfo);
				data.put(assignedTo, tmp);
			}
		}
		//获取待办处理所有的待办信息进行发送邮件提醒
		for (Map.Entry<String, List<BpmTaskInfo>> entry : data.entrySet()) {
			   String empId= entry.getKey();
			   List<BpmTaskInfo> tasks= entry.getValue();
			   if(StringUtils.isNotBlank(empId)){
				   //获取待办处理人
				  OrgEmployee employee=gdao.findById(OrgEmployee.class, empId);
				  if(employee!=null){
					  String email=employee.getEmail();
					  System.out.println("邮件提醒: "+employee.getNickName()+" email="+email);
					  //email="wuwd@gzsolartech.com";
					 if(StringUtils.isNotBlank(email)){
						 String taskList=taskInfo(tasks,employee.getNickName());
						 DatContentTemplate datContentTemplate=datContentTemplateService.loadByCode("BPMTIER01");
							String content="下列待办信息需要您及时处理:<br>";
							String title="每日待办信息提醒";
						 if(datContentTemplate!=null){
							title=datContentTemplate.getTitle();
							 content=datContentTemplate.getTextContent();
						}
						 //发送邮件
						 senEmailService.sendEmail(email,title,content+taskList,null);
					 }
				  }
			   }
			 }
	}
	
	/**
	 * 接收待办的内容信息
	 *@param bpmTaskInfos
	 *@param nickName
	 *@return
	 * @return void    返回类型 
	 * @throws
	 */
	public String taskInfo(List<BpmTaskInfo> bpmTaskInfos,String nickName){
		String content="<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\">";
		content+="<tr style='background:#DDEDFB;'>"
				+ "<th style='width:170px;'>申请单号</th>"
				+ "<th style='width: 150px;'>申请人</th>"
				+ "<th style='width:150px;' >当前处理人</th>"
				+ "<th style='width: 180px;'>当前环节</th>"
				+ "<th style='width: 170px;'>起草时间</th></tr>";
		for(BpmTaskInfo bpmTaskInfo:bpmTaskInfos){
			String documentId=bpmTaskInfo.getDocumentId();
			Map<String,Object> data=datDocumentService.getDocumentById(documentId);
			String host=bpmGlobalConfigService.getWebContext();
			//System.out.println("host:"+host);
			String href=host+"/console/template/engine/opendocument/"+data.get("__appid")+"/"+documentId+".xsp?mode=edit&taskId="+bpmTaskInfo.getTaskId();
			content+="<tr><th style='font-weight: normal;'>"
					+ "<a style=\"color:blue\" target=\"_blank\" href='"+href+"'>"+data.get("orderNum")+"</a></th>"
					+ "<th style='font-weight: normal;'>"+data.get("empName")+"</th>"
					+ "<th style='font-weight: normal;'>"+nickName+"</th>"
					+ "<th style='font-weight: normal;'>"+bpmTaskInfo.getTaskName()+"</th>"
					+ "<th style='font-weight: normal;'>"+data.get("_createTime")+"</th></tr>";
		}
		 content+="</table>";
		return  content;
	}
	
	/**
	 * 发送邮件通知
	 *@param recipientTmp 邮件接收者
	 *@param titleTmp  标题
	 *@param contentTmp 邮件内容
	 * @return void    返回类型 
	 * @throws
	 */
/*	public void sendEmail(final String recipientTmp,final String titleTmp,final String contentTmp){
		Map<String, Object> config = sysConfigurationService
				.getSysConfiguration(SysConfigurationTypeName.SYSTEM_CONFIG);
		final String SMTPAddress = config.get("SMTPAddress") + "";
		final String port = config.get("port") + "";
		final String email = config.get("email") + "";
		final String account = config.get("account") + "";
		final String password = config.get("password") + "";
		try {
			Runnable r = new Runnable() {
				public void run() {
					try {
						String result = MailUtil.sendMail(SMTPAddress,
								port, email, account, password,
								recipientTmp, titleTmp, contentTmp,
								true, false);
						new EmailNotificationUtil().execute(account, password, recipientTmp, titleTmp, contentTmp);
						//JSONObject jsoResult = new JSONObject(result);
					   //	if (!jsoResult.optBoolean("success", false)) {
					   //LOGGER.error("邮件发送失败" + result);
					   //	}
					} catch (Exception e) {
						LOGGER.error("邮件发送失败", e);
					}
				}
			};
			Thread t = new Thread(r);
			t.start();
		} catch (Exception e) {
			LOGGER.error("邮件发送失败", e);
		}
	}*/

}

