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

import com.gzsolartech.smartforms.entity.DatContentTemplate;
import com.gzsolartech.smartforms.entity.OrgEmployee;
import com.gzsolartech.smartforms.entity.bpm.BpmTaskInfo;
import com.gzsolartech.smartforms.service.BaseDataService;
import com.gzsolartech.smartforms.service.DatContentTemplateService;
import com.gzsolartech.smartforms.service.DatDocumentService;
import com.gzsolartech.smartforms.service.SysConfigurationService;
import com.gzsolartech.smartforms.service.bpm.BpmGlobalConfigService;
import com.gzsolartech.smartforms.service.bpm.BpmTaskInfoService;
import com.gzsolartech.smartforms.utils.FreeMarkUtil;
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
	private SendEmailService sendEmailService;
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
					  LOGGER.debug("邮件提醒: "+employee.getNickName()+" email="+email);
					  //email="wuwd@gzsolartech.com";
					 if(StringUtils.isNotBlank(email)){
						 DatContentTemplate datContentTemplate=datContentTemplateService.loadByCode("BPMTIER01");
							String content="下列待办信息需要您及时处理:<br>";
							String title="每日待办信息提醒";
						 if(datContentTemplate!=null){
							title=datContentTemplate.getTitle();
							 content=datContentTemplate.getTextContent();
						}
						 Map<String,Object> datas=taskInfo(tasks,employee.getNickName());
						content =FreeMarkUtil.renderTplBody(content, datas);
						 //发送邮件
						 sendEmailService.sendEmail(email,title,content,null);
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
	public Map<String,Object> taskInfo(List<BpmTaskInfo> bpmTaskInfos,String nickName){
		List<Map<String,Object>> documents=new ArrayList<Map<String,Object>>();
		String host=bpmGlobalConfigService.getWebContext();
		for(BpmTaskInfo bpmTaskInfo:bpmTaskInfos){
			String documentId=bpmTaskInfo.getDocumentId();
			if(StringUtils.isNotBlank(documentId)){
			Map<String,Object> data=datDocumentService.getDocumentById(documentId);
			String href=host+"/console/template/engine/opendocument/"+data.get("__appid")+"/"+documentId+".xsp?mode=edit&taskId="+bpmTaskInfo.getTaskId();
			data.put("_href", href);
			data.put("_taskName", bpmTaskInfo.getTaskName());
			data.put("_nickName", nickName);
			documents.add(data);
			}
		}
		Map<String,Object> datas=new HashMap<String,Object>();
		datas.put("datas",documents);
		return  datas;
	}
}

