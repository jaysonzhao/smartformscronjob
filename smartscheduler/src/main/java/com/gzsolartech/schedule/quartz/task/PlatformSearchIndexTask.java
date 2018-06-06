package com.gzsolartech.schedule.quartz.task;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.XML;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gzsolartech.bpmportal.entity.FormSearchIndexUpdateRecord;
import com.gzsolartech.schedule.service.PlatformSearchIndexService;
import com.gzsolartech.smartforms.constant.SysConfigurationTypeName;
import com.gzsolartech.smartforms.entity.DatDocument;
import com.gzsolartech.smartforms.entity.DatDocumentRight;
import com.gzsolartech.smartforms.entity.DatSearchForm;
import com.gzsolartech.smartforms.entity.DatSearchSystem;
import com.gzsolartech.smartforms.service.DatDocumentRightService;
import com.gzsolartech.smartforms.service.DatDocumentService;
import com.gzsolartech.smartforms.service.DatTableRowService;
import com.gzsolartech.smartforms.service.SearchManageService;
import com.gzsolartech.smartforms.service.SysConfigurationService;
import com.gzsolartech.smartforms.utils.HttpClientUtils;
import com.gzsolartech.smartforms.utils.XmlDataUtils;

/**
 * 定时调度将平台中管理的索引和绑定的表单文档同步至Es中
 * @author solar
 *
 */
@Component
public class PlatformSearchIndexTask extends BaseTask{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext=applicationContext;
		
	}

	@Override
	public void run(String jobId) {
		// TODO Auto-generated method stub
		SysConfigurationService sysConfigurationService = 
				applicationContext.getBean(SysConfigurationService.class);
		Map<String, Object> config = sysConfigurationService
				.getSysConfiguration(SysConfigurationTypeName.SYSTEM_CONFIG);
		
		PlatformSearchIndexService serivce = 
				applicationContext.getBean(PlatformSearchIndexService.class);
		
		SearchManageService  searchManageService = 
				applicationContext.getBean(SearchManageService.class);
		
		DatDocumentService  datDocumentService= 
				applicationContext.getBean(DatDocumentService.class);
		
		DatTableRowService	datTableRowService=
				applicationContext.getBean(DatTableRowService.class);
		
		DatDocumentRightService	datDocumentRightService=
				applicationContext.getBean(DatDocumentRightService.class);
		
		HttpClientUtils clientUtils=new HttpClientUtils();
		//获取平台创建的所有的索引信息
		List<DatSearchSystem>  datSearchSystems=serivce.getPlatfromManageIndex();
		for (DatSearchSystem  datSearchSystem:datSearchSystems) {
			
			List<DatDocument> docs=new ArrayList<DatDocument>();
			//判断激活状态的索引信息
			if(datSearchSystem.getDisable().equals("1")){
				//获取绑定的该索引绑定的表单
				List<DatSearchForm>  datSearchForms=	searchManageService.findBySearchId(datSearchSystem.getSearchId());
				
				//获取上一次更新的信息
				FormSearchIndexUpdateRecord  record=serivce.getUpdateInfo(datSearchSystem.getIndexs());
				
					if(record!=null){
						
						for (DatSearchForm datSearchForm:datSearchForms) {
							
							String [] stringArr= datSearchForm.getFormName().split(",");
							List<String> formnames=Arrays.asList(stringArr);
							
							for (int k = 0; k < formnames.size();k++) {
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								String updateTime=format.format(record.getUpdateTime());
								docs.addAll(serivce.getUpdateDocument(
										datSearchForm.getAppId(),formnames.get(k).trim(),updateTime));
							}
							
						}
						
						
					}else{
					 //没有更新记录的情况下，获取该索引下的表单组合文档
						for (DatSearchForm datSearchForm:datSearchForms) {
							
							String [] stringArr= datSearchForm.getFormName().split(",");
							List<String> formnames=Arrays.asList(stringArr);
							
							for (int k = 0; k < formnames.size();k++) {
								docs.addAll(datDocumentService.getDocumentByAppIdAndFormName(
										datSearchForm.getAppId(),formnames.get(k).trim()));
							}
							
						}
						
					}
				

				    FormSearchIndexUpdateRecord  newrecord=new FormSearchIndexUpdateRecord();
				    newrecord.setCreateTime(new Timestamp(System
							.currentTimeMillis()));
				    newrecord.setUpdateTime(new Timestamp(System
							.currentTimeMillis()));
				    newrecord.setId(UUID.randomUUID().toString());
				    newrecord.setIndexs(datSearchSystem.getIndexs());
				    serivce.save(newrecord);
					
			}
		
				for (DatDocument datDocument : docs) {
					
					datDocument.setDatApplication(null);
					//得到docdate
					 String docdata = XmlDataUtils.toString(datDocument
							.getDocumentData());
					 //清空docdate
					 datDocument.setDocumentData(null);
					 JSONObject  jsonObject= JSONObject.parseObject(datDocument.toString());	
					 
					 org.json.JSONObject jsoDocData = XML.toJSONObject(docdata);
					
					 if (jsoDocData.has("root")) {
								jsonObject.put("root", JSONObject.parse(jsoDocData.get("root").toString()));
					 }
					
					
					 Map<String, Object>  tableRows= datTableRowService.getDatas(datDocument.getDocumentId());
				     jsonObject.put("tableRows", tableRows);
				     
				     
					Map<String, Object> acl = new HashMap<String, Object>();
					List<String> empId = new ArrayList<String>();
					List<String> roleId = new ArrayList<String>();
					List<String>   depId =new ArrayList<String>();
				     
					List<DatDocumentRight> acls=		datDocumentRightService.getDocumentRights(datDocument.getDocumentId());
					for (int i = 0; i < acls.size(); i++) {
						if(acls.get(i).getRightObjectType().equals("employee")){
							empId.add(acls.get(i).getRightObjectId());
						}else if(acls.get(i).getRightObjectType().equals("department")){
							depId.add(acls.get(i).getRightObjectId());
						}else{
							roleId.add(acls.get(i).getRightObjectId());
						}
					}
					acl.put("empId", empId);
					acl.put("depId", depId);
					acl.put("roleId", roleId);
					jsonObject.put("acls",acl);
					
				    String url=config.get("searchCfg").toString()+"/"+datSearchSystem.getIndexs()
				    		+"/docinfo/"+datDocument.getDocumentId();
					
				    clientUtils.doPut(url,  JSON.toJSONString(jsonObject));
				    
				}
				
		  }
	}

}
