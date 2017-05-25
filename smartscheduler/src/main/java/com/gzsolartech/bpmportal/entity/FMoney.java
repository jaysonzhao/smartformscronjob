package com.gzsolartech.bpmportal.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * @description 从sql server 读取过来的数据 
 * @author hhf
 * @date 2017年5月24日 下午7:23:37
 */
@Entity
@Table(name = "F_MONEY")
public class FMoney {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	int id;
	@Column(name = "MONEYTYPE",length = 255)
	String moneyType;
	@Column(name = "MONEYCODE",length = 255)
	String moneyCode;
	@Column(name = "MONEYRATE",length = 255)
	String moneyRate;
	@Column(name = "MONEYSIGN",length = 255)
	String moneySign;
	@Column(name = "SORT")
	int sort;
	@Column(name = "CREATOR",length = 255)
	String creator;
	@Column(name = "CREATIONDATE")
	Timestamp creationDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}
	public String getMoneyCode() {
		return moneyCode;
	}
	public void setMoneyCode(String moneyCode) {
		this.moneyCode = moneyCode;
	}
	public String getMoneyRate() {
		return moneyRate;
	}
	public void setMoneyRate(String moneyRate) {
		this.moneyRate = moneyRate;
	}
	public String getMoneySign() {
		return moneySign;
	}
	public void setMoneySign(String moneySign) {
		this.moneySign = moneySign;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	
	
}
