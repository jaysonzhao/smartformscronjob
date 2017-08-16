package com.gzsolartech.bpmportal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.gzsolartech.smartforms.entity.OrgEmployee;

/**
 * AAC用户信息
 * AacEmployee entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AAC_EMPLOYEE")
public class AacEmployee implements java.io.Serializable {
	// Fields
	private String aacEmpId;
	private String empNum;
	private String area;
	private String areaen;
	private String positionlevel;
	private String dominousername;
	private String dominouserpsw;
	private String ceoNum;
	private String seniorvpNum;
	private String vpNum;
	private String seniordirectorNum;
	private String directorNum;
	private String seniormanagerNum;
	private String managerNum;
	private String empgrp;
	private String district;
	private String ad;
	private OrgEmployee orgEmployee;

	// Constructors

	/** default constructor */
	public AacEmployee() {
	}

	/** minimal constructor */
	public AacEmployee(String aacEmpId) {
		this.aacEmpId = aacEmpId;
	}

	/** full constructor */
	public AacEmployee(String aacEmpId, String empNum, String area,
			String areaen, String positionlevel, String dominousername,
			String dominouserpsw, String ceoNum, String seniorvpNum,
			String vpNum, String seniordirectorNum, String directorNum,
			String seniormanagerNum, String managerNum, String empgrp,
			String district, String ad) {
		this.aacEmpId = aacEmpId;
		this.empNum = empNum;
		this.area = area;
		this.areaen = areaen;
		this.positionlevel = positionlevel;
		this.dominousername = dominousername;
		this.dominouserpsw = dominouserpsw;
		this.ceoNum = ceoNum;
		this.seniorvpNum = seniorvpNum;
		this.vpNum = vpNum;
		this.seniordirectorNum = seniordirectorNum;
		this.directorNum = directorNum;
		this.seniormanagerNum = seniormanagerNum;
		this.managerNum = managerNum;
		this.empgrp = empgrp;
		this.district = district;
		this.ad = ad;
	}

	// Property accessors
	@Id
	@Column(name = "AAC_EMP_ID", unique = true, nullable = false, length = 100)
	public String getAacEmpId() {
		return this.aacEmpId;
	}

	public void setAacEmpId(String aacEmpId) {
		this.aacEmpId = aacEmpId;
	}

	@Column(name = "EMP_NUM", length = 200)
	public String getEmpNum() {
		return this.empNum;
	}

	public void setEmpNum(String empNum) {
		this.empNum = empNum;
	}

	@Column(name = "AREA", length = 200)
	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Column(name = "AREAEN", length = 200)
	public String getAreaen() {
		return this.areaen;
	}

	public void setAreaen(String areaen) {
		this.areaen = areaen;
	}

	@Column(name = "POSITIONLEVEL", length = 200)
	public String getPositionlevel() {
		return this.positionlevel;
	}

	public void setPositionlevel(String positionlevel) {
		this.positionlevel = positionlevel;
	}

	@Column(name = "DOMINOUSERNAME", length = 200)
	public String getDominousername() {
		return this.dominousername;
	}

	public void setDominousername(String dominousername) {
		this.dominousername = dominousername;
	}

	@Column(name = "DOMINOUSERPSW", length = 200)
	public String getDominouserpsw() {
		return this.dominouserpsw;
	}

	public void setDominouserpsw(String dominouserpsw) {
		this.dominouserpsw = dominouserpsw;
	}

	@Column(name = "CEO_NUM", length = 200)
	public String getCeoNum() {
		return this.ceoNum;
	}

	public void setCeoNum(String ceoNum) {
		this.ceoNum = ceoNum;
	}

	@Column(name = "SENIORVP_NUM", length = 200)
	public String getSeniorvpNum() {
		return this.seniorvpNum;
	}

	public void setSeniorvpNum(String seniorvpNum) {
		this.seniorvpNum = seniorvpNum;
	}

	@Column(name = "VP_NUM", length = 200)
	public String getVpNum() {
		return this.vpNum;
	}

	public void setVpNum(String vpNum) {
		this.vpNum = vpNum;
	}

	@Column(name = "SENIORDIRECTOR_NUM", length = 200)
	public String getSeniordirectorNum() {
		return this.seniordirectorNum;
	}

	public void setSeniordirectorNum(String seniordirectorNum) {
		this.seniordirectorNum = seniordirectorNum;
	}

	@Column(name = "DIRECTOR_NUM", length = 200)
	public String getDirectorNum() {
		return this.directorNum;
	}

	public void setDirectorNum(String directorNum) {
		this.directorNum = directorNum;
	}

	@Column(name = "SENIORMANAGER_NUM", length = 200)
	public String getSeniormanagerNum() {
		return this.seniormanagerNum;
	}

	public void setSeniormanagerNum(String seniormanagerNum) {
		this.seniormanagerNum = seniormanagerNum;
	}

	@Column(name = "MANAGER_NUM", length = 200)
	public String getManagerNum() {
		return this.managerNum;
	}

	public void setManagerNum(String managerNum) {
		this.managerNum = managerNum;
	}

	@Column(name = "EMPGRP", length = 200)
	public String getEmpgrp() {
		return this.empgrp;
	}

	public void setEmpgrp(String empgrp) {
		this.empgrp = empgrp;
	}

	@Column(name = "DISTRICT", length = 200)
	public String getDistrict() {
		return this.district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Column(name = "AD", length = 200)
	public String getAd() {
		return this.ad;
	}

	public void setAd(String ad) {
		this.ad = ad;
	}

	@Transient
	public OrgEmployee getOrgEmployee() {
		return orgEmployee;
	}

	public void setOrgEmployee(OrgEmployee orgEmployee) {
		this.orgEmployee = orgEmployee;
	}

}