package com.gzsolartech.bpmportal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzsolartech.bpmportal.entity.AacEmployee;
import com.gzsolartech.smartforms.constant.HttpSessionKey;
import com.gzsolartech.smartforms.entity.OrgEmployee;
import com.gzsolartech.smartforms.service.BaseDataService;
import com.gzsolartech.smartforms.service.OrgEmployeeService;

/**
 * AAC用户扩展信息
 * @author 
 *
 */
@Service("aacEmployeeService")
public class AACEmployeeService extends BaseDataService {
	private static final Logger LOG = LoggerFactory
			.getLogger(AACEmployeeService.class);
	@Autowired
	private OrgEmployeeService orgEmployeeService;
	
	/**
	 * 根据AD Name获取瑞声员工信息
	 * @adname AD名称
	 * @return
	 */
	public AacEmployee loadByAdName(String adname) {
		String hql="from AacEmployee where lower(ad)=?";
		AacEmployee aacemp=null;
		if (StringUtils.isNotBlank(adname)) {
			List<AacEmployee> emplist=gdao.queryHQL(hql,adname.toLowerCase());
			if (emplist!=null && !emplist.isEmpty()) {
				aacemp=emplist.get(0);
				//拿到AAC用户的扩展资料后，去OrgEmployee的信息
				OrgEmployee orgemp=orgEmployeeService.getUserByCN(aacemp.getAd());
				if (orgemp!=null) {
					aacemp.setOrgEmployee(orgemp);
				} else {
					aacemp.setOrgEmployee(new OrgEmployee());
				}
			}
		}
		return aacemp;
	}
	public Map leadership(String adname) {
//		王安民(empid:6079c376-19df-4528-9f93-246d1698a20a);
		AacEmployee employee = loadByAdName(adname);
		Map map = new HashMap();
		map.put("seniorVPNum", getEmployeeUnid(employee.getSeniorvpNum()));
		map.put("vpNum", getEmployeeUnid(employee.getVpNum()));
		map.put("seniorDirectorNum", getEmployeeUnid(employee.getSeniordirectorNum()));
		map.put("directorNum", getEmployeeUnid(employee.getDirectorNum()));
		map.put("seniorManagerNum", getEmployeeUnid(employee.getSeniormanagerNum()));
		map.put("managerNum", getEmployeeUnid(employee.getManagerNum()));
		System.out.println("========"+employee.getAd());
		map.put("directLeader", getEmployeeUnid(getDirectLeader(employee)));
		return map;
	}
	public Map leadershipInfo(String adname) {
		AacEmployee employee = loadByAdName(adname);
		Map map = new HashMap();
		Map tempMap = new HashMap();
		AacEmployee tempEmployee = loadByAdName(employee.getSeniorvpNum());
		if(tempEmployee!=null){
			tempMap.put("employeeUnid", tempEmployee.getOrgEmployee().getNickName()+"("+ tempEmployee.getOrgEmployee().getEmpNum()+")");
			tempMap.put("adname", tempEmployee.getAd());
			tempMap.put("companyNum", tempEmployee.getOrgEmployee().getCompanyNum());
			tempMap.put("deptNum", tempEmployee.getOrgEmployee().getEmpDept());
		}
		map.put("seniorVPNum", tempMap);
		
		tempEmployee = loadByAdName(employee.getVpNum());
		if(tempEmployee!=null){
			tempMap.put("employeeUnid", tempEmployee.getOrgEmployee().getNickName()+"("+ tempEmployee.getOrgEmployee().getEmpNum()+")");
			tempMap.put("adname", tempEmployee.getAd());
			tempMap.put("companyNum", tempEmployee.getOrgEmployee().getCompanyNum());
			tempMap.put("deptNum", tempEmployee.getOrgEmployee().getEmpDept());
		}else{
			tempMap.clear();
		}
		map.put("vpNum", tempMap);
		
		tempEmployee = loadByAdName(employee.getSeniordirectorNum());
		if(tempEmployee!=null){
			tempMap.put("employeeUnid", tempEmployee.getOrgEmployee().getNickName()+"("+ tempEmployee.getOrgEmployee().getEmpNum()+")");
			tempMap.put("adname", tempEmployee.getAd());
			tempMap.put("companyNum", tempEmployee.getOrgEmployee().getCompanyNum());
			tempMap.put("deptNum", tempEmployee.getOrgEmployee().getEmpDept());
		}else{
			tempMap.clear();
		}
		map.put("seniorDirectorNum", tempMap);
		
		tempEmployee = loadByAdName(employee.getDirectorNum());
		if(tempEmployee!=null){
			tempMap.put("employeeUnid", tempEmployee.getOrgEmployee().getNickName()+"("+ tempEmployee.getOrgEmployee().getEmpNum()+")");
			tempMap.put("adname", tempEmployee.getAd());
			tempMap.put("companyNum", tempEmployee.getOrgEmployee().getCompanyNum());
			tempMap.put("deptNum", tempEmployee.getOrgEmployee().getEmpDept());
		}else{
			tempMap.clear();
		}
		map.put("directorNum", tempMap);
		
		tempEmployee = loadByAdName(employee.getSeniormanagerNum());
		if(tempEmployee!=null){
			tempMap.put("employeeUnid", tempEmployee.getOrgEmployee().getNickName()+"("+ tempEmployee.getOrgEmployee().getEmpNum()+")");
			tempMap.put("adname", tempEmployee.getAd());
			tempMap.put("companyNum", tempEmployee.getOrgEmployee().getCompanyNum());
			tempMap.put("deptNum", tempEmployee.getOrgEmployee().getEmpDept());
		}else{
			tempMap.clear();
		}
		map.put("seniorManagerNum", tempMap);
		
		tempEmployee = loadByAdName(employee.getManagerNum());
		if(tempEmployee!=null){
			tempMap.put("employeeUnid", tempEmployee.getOrgEmployee().getNickName()+"("+ tempEmployee.getOrgEmployee().getEmpNum()+")");
			tempMap.put("adname", tempEmployee.getAd());
			tempMap.put("companyNum", tempEmployee.getOrgEmployee().getCompanyNum());
			tempMap.put("deptNum", tempEmployee.getOrgEmployee().getEmpDept());
		}else{
			tempMap.clear();
		}
		map.put("managerNum", tempMap);
		
		tempEmployee = loadByAdName(getDirectLeader(employee));
		if(tempEmployee!=null){
			tempMap.put("employeeUnid", tempEmployee.getOrgEmployee().getNickName()+"("+ tempEmployee.getOrgEmployee().getEmpNum()+")");
			tempMap.put("adname", tempEmployee.getAd());
			tempMap.put("companyNum", tempEmployee.getOrgEmployee().getCompanyNum());
			tempMap.put("deptNum", tempEmployee.getOrgEmployee().getEmpDept());
		}else{
			tempMap.clear();
		}
		map.put("directLeader", tempMap);
		return map;
	}
	/**
	 * 获取直接领导
	 * @param emp
	 * @return
	 */
	public  String getDirectLeader(AacEmployee emp){
		String DirectLeader="";
		DirectLeader = emp.getManagerNum();
		if(StringUtils.isEmpty(DirectLeader)){
			DirectLeader = emp.getSeniormanagerNum();
			if(StringUtils.isEmpty(DirectLeader)){
				DirectLeader = emp.getDirectorNum();
				if(StringUtils.isEmpty(DirectLeader)){
					DirectLeader = emp.getSeniordirectorNum();
					if(StringUtils.isEmpty(DirectLeader)){
						DirectLeader = emp.getVpNum();
						if(StringUtils.isEmpty(DirectLeader)){
							DirectLeader = emp.getSeniorvpNum();
							if(StringUtils.isEmpty(DirectLeader)){
								DirectLeader = emp.getCeoNum();
								if(StringUtils.isEmpty(DirectLeader)){
									DirectLeader="deadmin";
								}
							}
						}
					}
				}
			}
		}
		return DirectLeader;
	}
	private  String getEmployeeUnid(String adName){
		AacEmployee employee = loadByAdName(adName);
//		OrgEmployee oemployee.getOrgEmployee()
		String employeeUnid="";
		if(employee!=null){
			employeeUnid = employee.getOrgEmployee().getNickName()+"("+ employee.getOrgEmployee().getEmpNum()+")";
		}
		return employeeUnid;
	}

	/**
	 * 通过分公司companyNum获取对应的财务会计
	 */
	public List<Map<String, Object>> getFilialeaccountant(String companyNum){
		String [] numbers = (String[]) (companyNum==null?"":companyNum.split(","));
		List<Object> params = new ArrayList<Object>();
		//设置查询参数
		for(String number :numbers){
			params.add(number);
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("select  emp_name,nick_name from ORG_EMPLOYEE where EMP_NUM in(select EMP_NUM from ORG_ROLE_MEMBER where COMPANY_NUM");
		sb.append(" in (");
		//动态设置占位符
		for(int i=0 ;i<params.size();i++){
			if(i==0){
				sb.append("?");
			}else{
				sb.append(",?");
			}
		}
		sb.append(" ))");
		List<Map<String, Object>>  employee  = gdao.executeJDBCSqlQuery(sb.toString(), params);
		return employee;
	}
	
	/**
	 * 保存AAC人员信息，需要同时更新AacEmployee和OrgEmployee两个实体的数据
	 * @param aacEmpList  待更新AacEmployee实体列表
	 * @param orgEmpList  待更新OrgEmployee实体列表
	 */
	public void saveEmployeeInfo(List<AacEmployee> aacEmpList, List<OrgEmployee> orgEmpList) {
		if (!aacEmpList.isEmpty() && !orgEmpList.isEmpty() 
				&& aacEmpList.size()==orgEmpList.size()) {
			int size=aacEmpList.size();
			for (int i=0; i<size; i++) {
				AacEmployee aacemp=aacEmpList.get(i);
				gdao.saveOrUpdate(aacemp);
				OrgEmployee orgemp=orgEmpList.get(i);
				gdao.saveOrUpdate(orgemp);
				if (i>0 && i%50==0) {
					gdao.getSession().flush();
					gdao.getSession().clear();
				}
			}
		}
		gdao.getSession().flush();
		gdao.getSession().clear();
		LOG.info("AAC Employee List update success...");
		
	}
	
	public AacEmployee getEmployeeByEmpNum(String empNum) {
		AacEmployee employee=null;
		String hql="from AacEmployee where empNum=?";
		List<AacEmployee> emplist=gdao.queryHQL(hql, empNum);
		if (!emplist.isEmpty()) {
			employee=emplist.get(0);
		}
		return employee;
	}
	
	public List getDistrictFromEmployee() {
		Session session = gdao.getSession();
		String hql = "select id,name from Link";
		Query query = session.createQuery(hql); 
		// 默认查询出来的list里存放的是一个Object数组，还需要转换成对应的javaBean。
		List<Object[]> links = query.list();
		for (Object[] link : links) {
			String id = link[0].toString();
			String name = link[1].toString();
			System.out.println(id + " : " + name);
		}

		return null;
	}
	
	/**
	 * 判断当前用户是否属于管理员
	 * @param appId
	 * @return
	 */
	public Boolean isAppManager(String appId){
		String currenUser = (String) SecurityUtils.getSubject().getSession().getAttribute(HttpSessionKey.CURRENT_USER_NUM);
		
		/*//查询当前用户是否属于管理员
		String sql = "select * from DAT_APP_ACL where ACL_LEVEL = 'manager' and ACL_OBJECT_TYPE = 'employee'  and "
				+ "APP_ID = ?  and "
				+ "ACL_OBJECT_ID= ? ";
		
		
		//查询当前用户所属部门是否属于管理员
		
		String deptSql = "select * from DAT_APP_ACL where "
				+ "ACL_LEVEL = 'manager' and "
				+ "ACL_OBJECT_TYPE = 'department'  and "
				+ "APP_ID = ?  and ACL_OBJECT_ID in"
				
				//查找当前用户所属所有部门
				+ " (select od.DEPT_NUM from ORG_DEPT_MEMBER od ,ORG_EMPLOYEE  oe where oe.EMP_NUM = od.EMP_NUM and oe.EMP_NUM = ? ) ";
		
		//查询当前用户所属角色是否属于管理员
		String roleSql = "select * from DAT_APP_ACL where "
				+ "ACL_LEVEL = 'manager' and "
				+ "ACL_OBJECT_TYPE = 'role' and "
				+ "APP_ID = ?  and ACL_OBJECT_ID in "
				
				//查找当前用户所属所有角色
				+ "(select orm.ROLE_NUM  from ORG_ROLE_MEMBER orm ,ORG_EMPLOYEE  oe where oe.EMP_NUM = orm.EMP_NUM and oe.EMP_NUM = ? ) ";
		*/
		List<Object>  params  = new ArrayList<Object>();
		params.add(appId);
		params.add(currenUser);
		params.add(currenUser);
		params.add(currenUser);
		String sql = "select * from DAT_APP_ACL where ACL_LEVEL = 'manager' and ( ACL_OBJECT_TYPE = 'department' or ACL_OBJECT_TYPE = 'role' or ACL_OBJECT_TYPE = 'employee' ) "
				+ "and APP_ID = ? " 
				+ "and (ACL_OBJECT_ID "
				+ "in ( "
				//查询所属部门
				+ "(select od.DEPT_NUM from ORG_DEPT_MEMBER od ,ORG_EMPLOYEE  oe where oe.EMP_NUM = od.EMP_NUM and oe.EMP_NUM = ? ) union "
				//查询所属角色
				+ "(select orm.ROLE_NUM  from ORG_ROLE_MEMBER orm ,ORG_EMPLOYEE  oe where oe.EMP_NUM = orm.EMP_NUM and oe.EMP_NUM = ? ) "
				//查询当前登录人（人员）
				+ " ) or ACL_OBJECT_ID = ? )";
		
		List<Object> data = gdao.executeJDBCSqlQuery(sql, params);
		if(data.isEmpty()){
			return false;
		}
		return true;
	}
	
	
}
