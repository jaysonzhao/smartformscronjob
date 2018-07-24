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
	
	
	/**
	 * 按照模块搜索上一回更新的时间记录
	 * @param modular
	 * @return
	 */
	public DocSycnUpdateRecord getUpdateInfo(){
		List<DocSycnUpdateRecord> updateInfo = gdao.queryHQL(
				"from DocSycnUpdateRecord  order by createTime desc "
				);
		return updateInfo.size()>0?updateInfo.get(0):null;
	}

	/**
	 * 判断表单名索引是否存在
	 * @param index
	 * @return
	 */
	public boolean getIndex(String index){
		List<ESIndex> indexInfo = gdao.queryHQL(
				"from ESIndex  where indexs=? order by createTime desc "
				,index);
		return indexInfo.size()>0?true:false;
	}

	
	/**
	 * 保存更新时间
	 * @param record
	 */
	public void save(DocSycnUpdateRecord  record){
		gdao.save(record);
	}

	/**
	 * 保存表单索引记录
	 * @param index
	 */
	public void saveIndex(ESIndex  index){
		gdao.save(index);
	}
	/**
	 * 保存同步失败记录
	 * @param fail
	 */
	public void saveFail(List<DocSycnFailRecord> fail) {
		// TODO Auto-generated method stub
		gdao.save(fail);
	}
	/**
	 * 得到所有的文档信息
	 * @return
	 */
	public List<DatDocument> getAllDocument() {
		String hql = "from DatDocument  order by updateTime";
		List<DatDocument> docList = gdao.queryHQL(hql);
		return docList;
	}
	/**
	 * 获取上一回更新时间之后的文档信息
	 * @param updateTime
	 * @return
	 */
	public List<DatDocument> getDocumentByupdateTime(String updateTime) {
		String hql = "from DatDocument where updateTime >to_date('"+updateTime+"','YYYY-MM-DD HH24:MI:SS')";
		List<DatDocument> docList = gdao.queryHQL(hql);
		return docList;
	}


	
}
