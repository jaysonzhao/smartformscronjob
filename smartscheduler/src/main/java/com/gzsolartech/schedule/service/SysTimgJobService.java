package com.gzsolartech.schedule.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.gzsolartech.schedule.quartz.TaskServiceManager;
import com.gzsolartech.smartforms.dao.GenericDao;
import com.gzsolartech.smartforms.entity.DatApplication;
import com.gzsolartech.smartforms.entity.DetFormDefine;
import com.gzsolartech.smartforms.entity.DetFormField;
import com.gzsolartech.smartforms.entity.SysTimgJob;
import com.gzsolartech.smartforms.exceptions.SmartformsException;
import com.gzsolartech.smartforms.service.BaseDataService;
import com.gzsolartech.smartforms.service.DatApplicationService;
import com.gzsolartech.smartforms.service.DetFormDefineService;
import com.gzsolartech.smartforms.service.DetFormFieldService;

/**
 * 
 * @ClassName: SysTimgJobService
 * @Description: 任务调度业务逻辑处理
 * @author wwd
 * @date 2016年8月22日 下午1:53:33
 *
 */
@Service
public class SysTimgJobService  extends BaseDataService implements
		ApplicationContextAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	protected GenericDao gdao;
	private static ApplicationContext applicationContext;
	@Autowired
	private DetFormFieldService detFormFieldService;
	@Autowired
	private DatApplicationService datApplicationService;
	@Autowired
	private DetFormDefineService detFormDefineService;
	@Autowired
	private TaskServiceManager taskServiceManager;

	/**
	 * 
	 * @Title: getScheduleJob
	 * @Description: 获取一个job
	 * @param @param jobId
	 * @param @return 设定文件
	 * @return SysTimgJob 返回类型
	 * @throws
	 */
	public SysTimgJob getScheduleJob(String jobId) {
		SysTimgJob job = gdao.findById(SysTimgJob.class, jobId);
		return job;
	}

	/**
	 * 
	 * @Title: updateSysTimgJob
	 * @Description: 更新一个job
	 * @param @param job 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void updateSysTimgJob(SysTimgJob job) {
		gdao.update(job);
	}

	/**
	 * 
	 * @Title: getAll
	 * @Description: 获取所有的job
	 * @param @return 设定文件
	 * @return List<SysTimgJob> 返回类型
	 * @throws
	 */
	public List<SysTimgJob> getAll() {
		List<SysTimgJob> scheduleJob = gdao.findAll(SysTimgJob.class);
		return scheduleJob;
	}

	/**
	 * 
	 * @Title: findByPage
	 * @Description: 分页获取数据
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	public Map<String, Object> findByPage(int page, int rows) {
		Map<String, Object> dataResult = new HashMap<String, Object>();
		List<SysTimgJob> scheduleJobs = gdao.findByPage(
				"from  SysTimgJob order by updateTime desc", page, rows);
		Long conut = gdao.countAll(SysTimgJob.class);
		dataResult.put("total", conut);
		dataResult.put("rows", scheduleJobs);
		return dataResult;
	}

	/**
	 * @throws Exception
	 * 
	 * @Title: saveOrUpdate
	 * @Description: 更新或修改一个job
	 * @param @param jobId
	 * @param @param jobName
	 * @param @param cronExpression
	 * @return void 返回类型
	 * @throws
	 */
	public String saveOrUpdate(SysTimgJob sysTimgJob) throws Exception {
		String jobId="";
		if (StringUtils.isBlank(sysTimgJob.getJobId())) {
			jobId=UUID.randomUUID().toString();
			sysTimgJob.setJobId(jobId);
			sysTimgJob.setJobGroup(UUID.randomUUID().toString());
			sysTimgJob.setJobStatus("2");
			gdao.save(sysTimgJob);
		} else {
			SysTimgJob job = gdao.findById(SysTimgJob.class,sysTimgJob.getJobId());
			if(job!=null){
			jobId=sysTimgJob.getJobId();
			sysTimgJob.setJobStatus("2");
			BeanUtils.copyProperties(sysTimgJob, job, "createTime", "creator",
					"lastRunTime", "startRunTime");
			gdao.update(job);
			}else{
				throw new SmartformsException("无法根据任务id找到任务");
			}
			try {
				taskServiceManager.deleteJob(sysTimgJob.getJobId());
			} catch (Exception e) {
			}
		}
		return jobId;
	}

	/**
	 * @throws Exception 
	 * 
	 * @Title: deleteJob
	 * @Description: 删除调度任务信息
	 * @return void 返回类型
	 * @throws
	 */
	public void deleteJob(String[] jobs) throws Exception {
		for (String jobId : jobs) {
			SysTimgJob sysTimgJob = getScheduleJob(jobId);
			if (sysTimgJob != null) {
				gdao.delete(sysTimgJob);
			}else{
				throw new Exception("无法根据任务ID="+jobId+" 查询到任务信息，删除失败");
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SysTimgJobService.applicationContext = applicationContext;
	}

	/**
	 * 
	 * @Title: getTasks
	 * @Description: 获取调度任务
	 * @return List<String> 返回类型
	 * @throws
	 */
	public List<String> getTasks() {
		List<String> tasks = new ArrayList<String>();
		String[] beanDefinitionNames = applicationContext
				.getBeanDefinitionNames();
		for (int i = 0; i < beanDefinitionNames.length; i++) {
			String name = applicationContext.getBean(beanDefinitionNames[i])
					.getClass().getName();
			
			if (name.startsWith("com.gzsolartech.schedule.quartz.task")) {
				String tmp=beanDefinitionNames[i];
				if (!"BaseTask".equals(tmp)) {
					tasks.add(beanDefinitionNames[i]);
				}
			}
		}
		return tasks;
	}

	/**
	 * 
	 * @Title: getAppName
	 * @Description: 获取应用库名称
	 * @return String 返回类型
	 * @throws
	 */
	public String getAppName(String appId) throws Exception {
		DatApplication datApplication = datApplicationService.load(appId);
		if (datApplication != null) {
			return datApplication.getAppName();
		} else {
			throw new Exception("找不到该应用库:appid:" + appId);
		}
	}

	public String getFormName(String formId) throws Exception {
		DetFormDefine detFormDefine = detFormDefineService.load(formId);
		if (detFormDefine != null) {
			return detFormDefine.getFormName();
		} else {
			throw new Exception("找不到该表单:formId:" + formId);
		}
	}

	/**
	 * 
	 * @Title: beforeSync
	 * @Description: 数据同步前执行的校验
	 * @return String 返回类型
	 * @throws
	 */
	public String beforeSync(String tableName, String formId) {
		List<DetFormField> fields = detFormFieldService.listAll(formId);
		if (!tableExist(tableName)) {
			String tableSql = "create table " + tableName + "(";
			for (DetFormField detFormField : fields) {
				tableSql += detFormField.getFieldName()
						+ " "
						+ typeChange(detFormField.getFieldType(),
								detFormField.getFieldLength()) + " ,";
			}
			tableSql += "DOCUMENT_ID VARCHAR2(100 BYTE) NOT NULL , "
					+ "FORM_NAME VARCHAR2(100 BYTE) NOT NULL , "
					+ "PARENT_DOCUMENT_ID VARCHAR2(100 BYTE), "
					+ "CREATE_TIME DATE, " + "CREATOR VARCHAR2(100 BYTE), "
					+ "UPDATE_TIME DATE, " + "UPDATE_BY VARCHAR2(100 BYTE),"
					+ "APP_ID VARCHAR2(100 BYTE) NOT NULL , "
					+ "READ_RIGHT_PASS_ON VARCHAR2(20 BYTE), "
					+ "WRITE_RIGHT_PASS_ON VARCHAR2(20 BYTE), "
					+ "primary key (DOCUMENT_ID))";
			gdao.executeJDBCSql(tableSql);
		} else {
			for (DetFormField detFormField : fields) {
				List<Object> field = gdao
						.executeJDBCSqlQuery("SELECT * FROM USER_TAB_COLUMNS WHERE TABLE_NAME = '"
								+ tableName.toUpperCase()
								+ "' AND COLUMN_NAME = '"
								+ detFormField.getFieldName().toUpperCase()
								+ "'");
				if (field.isEmpty()) {
					String temp = "alter table "
							+ tableName.toUpperCase()
							+ " add("
							+ detFormField.getFieldName()
							+ "  "
							+ typeChange(detFormField.getFieldType(),
									detFormField.getFieldLength()) + ")";
					gdao.executeJDBCSql(temp);
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @Title: tableExist
	 * @Description: 判断表是否存在
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean tableExist(String tableName) {
		String tableExist = "select count(*) COUNT from all_tables  where TABLE_NAME =?";
		List<Object> params = new ArrayList<Object>();
		params.add(tableName.toUpperCase());
		List<Object> datas = gdao.executeJDBCSqlQuery(tableExist, params);
		Map<String, BigDecimal> count = (Map<String, BigDecimal>) datas.get(0);
		Long temp = count.get("COUNT").longValue();
		return !(temp == 0);
		/*if (temp == 0) {
			return false;
		} else {
			return true;
		}*/

	}

	/**
	 * 
	 * @Title: typeChange
	 * @Description: 创建表生成 字段的类型
	 * @return String 返回类型
	 * @throws
	 */
	public String typeChange(String type, String fieldLength) {
		if ("int".equals(type)) {
			return "int";
		} else if ("text".equals(type)) {
			if (StringUtils.isBlank(fieldLength)) {
				fieldLength = "255";
			}
			return "VARCHAR2(" + fieldLength + " BYTE)";
		} else if ("double".equals(type)) {
			return "number(10,3)";
		} else {
			return "VARCHAR2(255 BYTE)";
		}
	}

	/**
	 * 
	 * @Title: sysncSql
	 * @Description: 生成数据同步的sql语句
	 * @return String 返回类型
	 * @throws
	 */
	public static String sysncSql(List<DetFormField> fields,
			SysTimgJob sysTimgJob) {
		String detFormFields = " XMLTABLE ('/root' PASSING t.document_data COLUMNS ";
		for (int i = 0; i < fields.size(); i++) {
			DetFormField fd = fields.get(i);
			String fdname = fd.getFieldName();
			detFormFields += fdname + "  PATH '" + fdname + "', ";
		}
		detFormFields += "formname  PATH '__formname' ) x";
		String fieldSql = "DOCUMENT_ID, FORM_NAME , PARENT_DOCUMENT_ID , CREATE_TIME , CREATOR, UPDATE_TIME , "
				+ "UPDATE_BY ,APP_ID ,READ_RIGHT_PASS_ON , WRITE_RIGHT_PASS_ON";
		String sql = "select " + fieldSql + ",x.* from DAT_DOCUMENT " + " t ,"
				+ detFormFields + " where FORM_NAME=? and app_id=? ";
		if (StringUtils.isNotBlank(sysTimgJob.getStartRunTime())) {
			sql += " and update_time>=TO_DATE('" + sysTimgJob.getStartRunTime()
					+ "','YYYY-MM-DD HH24:mi:ss') ";
		}
		sql += " order by update_time";
		return sql;
	}

	/**
	 * 
	 * @Title: syncData
	 * @Description: 数据同步
	 * @return void 返回类型
	 * @throws
	 */
	public void syncData(String tableName, String appId, String formId,
			String formName, SysTimgJob sysTimgJob) {
		List<DetFormField> fields = detFormFieldService.listAll(formId);
		List<Object> params = new ArrayList<Object>();
		params.add(formName);
		params.add(appId);
		int i = 1;
		while (true) {
			List<Map<String, Object>> datas = gdao.executeJDBCSqlQuery(
					sysncSql(fields, sysTimgJob), i, 100, params);
			insertData(tableName, fields, datas);
			i++;
			if (datas.size() < 100) {
				break;
			}
		}

		/**
		 * 删除已经删除的数据
		 */
		if (StringUtils.isNotBlank(sysTimgJob.getStartRunTime())) {
			String deleteSql = "delete from "
					+ tableName
					+ " where DOCUMENT_ID in (select DOCUMENT_ID from DAT_DELETED_DOCUMENT ) and  update_time>=TO_DATE('"
					+ sysTimgJob.getStartRunTime()
					+ "','YYYY-MM-DD HH24:mi:ss')";
			gdao.executeJDBCSql(deleteSql);
		}

	}

	/**
	 * 
	 * @Title: insertData
	 * @Description: 数据同步 插入新的数据
	 * @return void 返回类型
	 * @throws
	 */
	public void insertData(String tableName, List<DetFormField> fields,
			List<Map<String, Object>> datas) {
		String defaultField = "DOCUMENT_ID, FORM_NAME , PARENT_DOCUMENT_ID , CREATE_TIME , CREATOR, UPDATE_TIME , "
				+ "UPDATE_BY ,APP_ID ,READ_RIGHT_PASS_ON , WRITE_RIGHT_PASS_ON";
		String defaultFields[] = defaultField.split(",");

		for (Map<String, Object> data : datas) {
			String documentId = (String) data.get("DOCUMENT_ID");
			List<Object> params = new ArrayList<Object>();
			params.add(documentId);
			List<Map<String, Object>> entity = gdao.executeJDBCSqlQuery(
					"select * from " + tableName + " where DOCUMENT_ID=?",
					params);
			if (entity.isEmpty()) {
				String inserSql = "insert into " + tableName + " (";
				String insertSqlValue = " values (";
				List<Object> insertData = new ArrayList<Object>();
				for (String field : defaultFields) {
					inserSql += field.trim() + ",";
					insertSqlValue += "?,";
					insertData.add(data.get(field.trim()));
				}
				for (DetFormField detFormField : fields) {
					inserSql += detFormField.getFieldName() + ",";
					insertSqlValue += "?,";
					insertData.add(data.get(detFormField.getFieldName()
							.toUpperCase()));
				}
				if (inserSql.endsWith(",")) {
					inserSql = inserSql.substring(0, inserSql.length() - 1);
				}

				if (insertSqlValue.endsWith(",")) {
					insertSqlValue = insertSqlValue.substring(0,
							insertSqlValue.length() - 1);
				}
				inserSql = inserSql + ") " + insertSqlValue + ")";
				gdao.executeJDBCSql(inserSql, insertData);
			} else {
				updateData(tableName, fields, data, defaultFields);
			}
		}
	}

	/**
	 * 
	 * 
	 * @Title: updateData
	 * @Description: 数据同步 存在时更新数据
	 * @return void 返回类型
	 * @throws
	 */
	public void updateData(String tableName, List<DetFormField> fields,
			Map<String, Object> data, String defaultFields[]) {
		String updateSql = "UPDATE " + tableName + " SET  ";
		List<Object> updateData = new ArrayList<Object>();
		for (String field : defaultFields) {
			updateSql += field.trim() + "=?,";
			updateData.add(data.get(field.trim()));
		}

		for (DetFormField detFormField : fields) {
			updateSql += detFormField.getFieldName() + "=?,";
			updateData.add(data.get(detFormField.getFieldName().toUpperCase()));
		}

		if (updateSql.endsWith(",")) {
			updateSql = updateSql.substring(0, updateSql.length() - 1);
		}
		updateData.add(data.get("DOCUMENT_ID"));
		updateSql += " where DOCUMENT_ID=?";
		gdao.executeJDBCSql(updateSql, updateData);
	}

	/**
	 * 获取最新一条的数据更新时间
	 * 
	 * @Title: getLastUpdateTime
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return String 返回类型
	 * @throws
	 */
	public String getLastUpdateTime(String tableName) {
		String sql = "select to_char(update_Time,'yyyy-MM-dd hh24:mi:ss') update_Time,rownum from ("
				+ "select update_Time,rownum from "
				+ tableName
				+ " order by update_Time desc" + ") where rownum=1";
		List<Map<String, Object>> data = gdao.executeJDBCSqlQuery(sql);
		if (data.isEmpty()) {
			return null;
		} else {
			return (String) data.get(0).get("UPDATE_TIME");
		}
	}
	
	
}
