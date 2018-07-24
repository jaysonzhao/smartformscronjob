package com.gzsolartech.bpmportal.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "ES_INDEX")
public class ESIndex {

	//ID
	private String id;
	//索引
	private String  indexs;
 
	//调度执行时间
	private Timestamp createTime;
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 100)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "INDEXS", length = 100)
	public String getIndexs() {
		return indexs;
	}
	public void setIndexs(String indexs) {
		this.indexs = indexs;
	}
	 
	@Column(name = "CREATE_TIME", length = 7)
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	} 

}
