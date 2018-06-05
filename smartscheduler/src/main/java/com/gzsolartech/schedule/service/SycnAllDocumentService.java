package com.gzsolartech.schedule.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gzsolartech.bpmportal.entity.DocSycnFailRecord;
import com.gzsolartech.bpmportal.entity.DocSycnUpdateRecord;
import com.gzsolartech.bpmportal.entity.ESIndex;
import com.gzsolartech.smartforms.entity.DatDocument;
import com.gzsolartech.smartforms.service.BaseDataService;
@Service
public class SycnAllDocumentService  extends BaseDataService {
	
	
	
	public DocSycnUpdateRecord getUpdateInfo(){
		List<DocSycnUpdateRecord> updateInfo = gdao.queryHQL(
				"from DocSycnUpdateRecord  order by createTime desc "
				);
		return updateInfo.size()>0?updateInfo.get(0):null;
	}

	
	public boolean getIndex(String index){
		List<ESIndex> indexInfo = gdao.queryHQL(
				"from ESIndex  where indexs=? order by createTime desc "
				,index);
		return indexInfo.size()>0?true:false;
	}

	
	
	public void save(DocSycnUpdateRecord  record){
		gdao.save(record);
	}

	public void saveIndex(ESIndex  index){
		gdao.save(index);
	}
	public void saveFail(List<DocSycnFailRecord> fail) {
		// TODO Auto-generated method stub
		gdao.save(fail);
	}
	
	public List<DatDocument> getAllDocument() {
		String hql = "from DatDocument  order by updateTime";
		List<DatDocument> docList = gdao.queryHQL(hql);
		return docList;
	}
	
	public List<DatDocument> getDocumentByupdateTime(String updateTime) {
		String hql = "from DatDocument where updateTime >to_date('"+updateTime+"','YYYY-MM-DD HH24:MI:SS')";
		List<DatDocument> docList = gdao.queryHQL(hql);
		return docList;
	}


	
}
