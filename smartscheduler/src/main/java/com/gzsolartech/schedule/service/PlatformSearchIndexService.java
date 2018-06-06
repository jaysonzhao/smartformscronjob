package com.gzsolartech.schedule.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gzsolartech.bpmportal.entity.DocSycnUpdateRecord;
import com.gzsolartech.bpmportal.entity.FormSearchIndexUpdateRecord;
import com.gzsolartech.smartforms.entity.DatApplication;
import com.gzsolartech.smartforms.entity.DatDocument;
import com.gzsolartech.smartforms.entity.DatSearchSystem;
import com.gzsolartech.smartforms.service.BaseDataService;

@Service
public class PlatformSearchIndexService extends BaseDataService {

	
	/**
	 * 获取平台管理的所有索引
	 * @return
	 */
	public List<DatSearchSystem>  getPlatfromManageIndex(){
		List<DatSearchSystem> datSearchInfos = gdao.queryHQL(
				"from DatSearchSystem    order by createTime desc "
				);
		return datSearchInfos;
		
	}
	
	/**
	 * 获取上回更新时间
	 * @param indexs
	 * @return
	 */
	public FormSearchIndexUpdateRecord getUpdateInfo(String indexs){
		List<FormSearchIndexUpdateRecord> updateInfo = gdao.queryHQL(
				"from FormSearchIndexUpdateRecord  where indexs=?  order by createTime desc "
				,indexs);
		return updateInfo.size()>0?updateInfo.get(0):null;
	}
	
	
	
	/**
	 * 根据表单名和应用库id获取文档内容
	 * @param datAppId
	 * @return
	 */
	public List<DatDocument> getUpdateDocument(String datAppId,String formName,String updateTime) {
		DatApplication datapp = new DatApplication();
		datapp.setAppId(datAppId);
		String hql = "from DatDocument where datApplication=? and formName=?  and  "
				+ " updateTime >to_date('"+updateTime+"','YYYY-MM-DD HH24:MI:SS')   order by createTime desc";
		List<DatDocument> docList = gdao.queryHQL(hql, datapp,formName);
		
		return docList;
	}
	
	/**
	 * 保存索引更新时间
	 * @param record
	 */
	public void save(FormSearchIndexUpdateRecord record){
		
		gdao.save(record);
	}
	
}
