package com.gzsolartech.bpmportal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzsolartech.bpmportal.util.XMLClient;
import com.gzsolartech.smartforms.entity.DatApplication;
import com.gzsolartech.smartforms.entity.DatDocument;
import com.gzsolartech.smartforms.service.BaseDataService;
import com.gzsolartech.smartforms.service.DatApplicationService;
import com.gzsolartech.smartforms.service.DatDocumentService;
import com.gzsolartech.smartforms.utils.DatDocumentUtil;
import com.gzsolartech.smartforms.utils.XmlDataUtils;


/**
 * 
 *用于KronosAPI的调用
 * @author 朱华虎
 * 
 */
@Service("kronosService")
public class KronosService extends BaseDataService {
private static final Logger LOG = LoggerFactory
			.getLogger(KronosService.class);
/**
 * method：getunapprovehours
 * 用途：从考勤数据中获取加班时数
 * @param empNum
 * 员工编号
 * @param date
 * 日期
 **/
	@Autowired
	private DatApplicationService datApplicationService;
	@Autowired
	private DatDocumentService datDocumentService;
	
	public JSONObject getunapprovehours(String empNum,String date) throws Exception{
			XMLClient api = new XMLClient();
			api.open(getKronosConfig());
			String xml= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+"<Kronos_WFC Version=\"1.0\">"
					+"<Request Action=\"QueryData\">"
					+"<CNAPI SQLKEY=\"com.kronos.wfc.timekeeping.cncus.aac.api.overtimeutils.getunapprovehours\">"
					+"<APIParameters>"
					+"<APIParameter Name =\"PersonNum\" Value=\""+empNum+"\" Type=\"String\" index=\"0\" />"
					+"<APIParameter Name =\"StartDate\" Value=\""+date+"\" Type=\"Datetime\" index=\"1\" />"
					+"</APIParameters>"
					+"</CNAPI>"
					+"</Request>"
					+"</Kronos_WFC>";
			api.sendRequest(xml);
			JSONObject resposeJson = XML.toJSONObject(api.getXmlReply()).getJSONObject("Kronos_WFC").getJSONObject("Response");
			api.close();
			return resposeJson;
	}
	
	/**
	 * method:checkOverTime
	 * 用于加班加班校验
	 * @param empNum
	 * 员工编号
	 * @param startTime
	 * 开始时间用于校验加班时间
	 * @param endTime
	 * 结束时间用于校验加班时间
	 * @param date
	 * 用于判断是否节假日
	 * @return 
	 * JSONObject
	 * value=0：不可发起申请（填写的时间段在班制内，或者5.5天班制的周六上班时数未达到规定时数）  
	 * value=1：用户可以自由选择加班用于调休或者加班费
	 * value=2：用户仅能选择用于调休
	 * value=3：用户技能选择用于加班费
	 **/
	public JSONObject checkOverTime(String empNum,String date,String startTime,String endTime) throws Exception{
		XMLClient api = new XMLClient();
		api.open(getKronosConfig());
		String xml= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+"<Kronos_WFC Version=\"1.0\">"
				+"<Request Action=\"QueryData\">"
				+"<CNAPI SQLKEY=\"com.kronos.wfc.timekeeping.cncus.aac.api.overtimeutils.checkovertime\">"
				+"<APIParameters>"
				+"<APIParameter index=\"0\" Value=\""+empNum+"\" Name=\"PersonNum\" Type=\"String\"/>"
				+"<APIParameter index=\"1\" Value=\""+startTime+"\" Name=\"startTime\" Type=\"String\"/>"
				+"<APIParameter index=\"2\" Value=\""+endTime+"\" Name=\"endTime\" Type=\"String\"/>"
				+"<APIParameter index=\"3\" Value=\""+empNum+"\" Name=\"PersonNum2\" Type=\"String\"/>"
				+"<APIParameter index=\"4\" Value=\""+date+"\" Name=\"Date\" Type=\"String\"/>"
				+"</APIParameters>"
				+"</CNAPI>"
				+"</Request>"
				+"</Kronos_WFC>";
		api.sendRequest(xml);
		JSONObject resposeJson = XML.toJSONObject(api.getXmlReply()).getJSONObject("Kronos_WFC").getJSONObject("Response");
		api.close();
		return resposeJson;
	}
	/**
	 * 
	 * @param empNum
	 * 员工编号
	 * @param LeaveType
	 * 假期类型
	 * @param startDate
	 * @param startTime
	 * 开始时间
	 * @param endDate
	 * @param endTime
	 * 结束时间
	 * @return
	 * @throws Exception
	 */
	public JSONObject xiujia(String empNum,String LeaveType,String startDate,String startTime,String endDate,String endTime) throws Exception{
		XMLClient api = new XMLClient();
		api.open(getKronosConfig());
		String xml= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+"<Kronos_WFC Version=\"1.0\">"
				+"<Request Action=\"Update\">"
				+"<CNLeaveRequest LeaveType=\""+LeaveType+"\" StartDate=\""+startDate+"\" StartTime=\""+startTime+"\" EndDate=\""+endDate+"\" EndTime=\""+endTime+"\" TBLName=\"\">"
				+"<Employee>"
				+"<PersonIdentity PersonNumber=\""+empNum+"\" />"
				+"</Employee>"
				+"</CNLeaveRequest>"
				+"</Request>"
				+"</Kronos_WFC>";
		api.sendRequest(xml);
		JSONObject resposeJson = XML.toJSONObject(api.getXmlReply()).getJSONObject("Kronos_WFC").getJSONObject("Response");
		api.close();
		return resposeJson;
	}
	/**
	 * 获取假期类型
	 * @return
	 * @throws Exception
	 */
	public JSONObject getAllLeave(String empNum,String date) throws Exception{
		XMLClient api = new XMLClient();
		api.open(getKronosConfig());
		String xml= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+"<Kronos_WFC Version=\"1.0\">"
				+"<Request Action=\"QueryData\">"
				+"<CNAPI SQLKEY=\"com.kronos.wfc.timekeeping.cncus.aac.api.leaverequest.getaLLLeaveCode\">"
				+"<APIParameters>"
				+"<APIParameter index=\"0\" Value=\""+empNum+"\" Name=\"PersonNum\" Type=\"String\"/>"
				+"<APIParameter index=\"1\" Value=\""+date+"\" Name=\"Date\" Type=\"String\"/>"
				+"</APIParameters>"
				+"</CNAPI>"
				+"</Request>"
				+"</Kronos_WFC>";
		api.sendRequest(xml);
		JSONObject resposeJson = XML.toJSONObject(api.getXmlReply()).getJSONObject("Kronos_WFC").getJSONObject("Response");
		api.close();
		return resposeJson;
	}
	/**
	 * 用于获取剩余调休假、去年剩余年假、今年剩余年假
	 * @param empNum
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public JSONObject getAvailableTimes(String empNum,String date) throws Exception{
		XMLClient api = new XMLClient();
		api.open(getKronosConfig());
		String xml= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+"<Kronos_WFC Version=\"1.0\">"
					+"<Request Action=\"Load\">"
				    +"<AccrualData BalanceDate=\""+date+"\">"
				    +"<Employee>"
				    +"<PersonIdentity PersonNumber=\""+empNum+"\" />"
				    +"</Employee>"
				    +"</AccrualData>"
				    +"</Request>"
				    +"</Kronos_WFC>";
		api.sendRequest(xml);
		JSONObject resposeJson = XML.toJSONObject(api.getXmlReply()).getJSONObject("Kronos_WFC").getJSONObject("Response");
		api.close();
		return resposeJson;
	}
	
	/**
	 * 用于判断是否法定节假日
	 * @param empNum
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public JSONObject getIsHoliday(String empNum,String date) throws Exception{
		XMLClient api = new XMLClient();
		api.open(getKronosConfig());
		String xml= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+"<Kronos_WFC Version=\"1.0\">"
				+"<Request Action=\"QueryData\">"
				+"<CNAPI SQLKEY=\"com.kronos.wfc.timekeeping.cncus.aac.api.overtimeutils.getisholiday\">"
				+"<APIParameters>"
				+"<APIParameter index=\"0\" Value=\""+empNum+"\" Name=\"PersonNum\" Type=\"String\"/>"
				+"<APIParameter index=\"1\" Value=\""+date+"\" Name=\"Date\" Type=\"String\"/>"
				+"</APIParameters>"
				+"</CNAPI>"
				+"</Request>"
				+"</Kronos_WFC>";
		api.sendRequest(xml);
		JSONObject resposeJson = XML.toJSONObject(api.getXmlReply()).getJSONObject("Kronos_WFC").getJSONObject("Response");
		api.close();
		return resposeJson;
	}
	/**
	 * 获取员工签核日期
	 * @param empNum
	 * @return
	 * @throws Exception
	 */
	public JSONObject getSigOffDay(String empNum) throws Exception{
		XMLClient api = new XMLClient();
		api.open(getKronosConfig());
		String xml=  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+"<Kronos_WFC Version=\"1.0\">"
				+"<Request Action=\"QueryData\">"
				+"<CNAPI SQLKEY=\"com.kronos.wfc.timekeeping.cncus.aac.api.pubutils.getsigoffday\">"
				+"<APIParameters>"
				+"<APIParameter index=\"0\" Value=\""+empNum+"\" Name=\"PersonNum\" Type=\"String\"/>"
				+"</APIParameters>"
				+"</CNAPI>"
				+"</Request>"
				+"</Kronos_WFC>";
		api.sendRequest(xml);
		JSONObject resposeJson = XML.toJSONObject(api.getXmlReply()).getJSONObject("Kronos_WFC").getJSONObject("Response");
		api.close();
		return resposeJson;
	}
	/**
	 * 休假写入考勤系统
	 * @param empNum
	 * @param LeaveType
	 * @param startDate
	 * @param startTime
	 * @param endDate
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public void saveLeave(String empNum,String LeaveType,String startDate,String startTime,String endDate,String endTime) throws Exception{
		XMLClient api = new XMLClient();
		api.open(getKronosConfig());
		String xml= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+"<Kronos_WFC Version=\"1.0\">"
				+"<Request Action=\"Update\">"
				+"<CNLeaveRequest LeaveType=\""+LeaveType+"\" StartDate=\""+startDate+"\" StartTime=\""+startTime+"\" EndDate=\""+endDate+"\" EndTime=\""+endTime+"\" TBLName=\"\">"
				+"<Employee>"
				+"<PersonIdentity PersonNumber=\""+empNum+"\" />"
				+"</Employee>"
				+"</CNLeaveRequest>"
				+"</Request>"
				+"</Kronos_WFC>";
		api.sendRequest(xml);
		JSONObject resposeJson = XML.toJSONObject(api.getXmlReply()).getJSONObject("Kronos_WFC").getJSONObject("Response");
		System.out.println("写入考勤:"+resposeJson);
		api.close();
//		return resposeJson;
	}
/**
 * 加班预审批
 * @param empNum
 * @param date
 * @param AmountInTime
 * @return
 * @throws Exception
 */
	public void ApproveSome(JSONArray jarr) throws Exception{
		XMLClient api = new XMLClient();
		api.open(getKronosConfig());
		String xml= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+"<Kronos_WFC Version=\"1.0\">";
		for(int i=0;i<jarr.length();i++){
			JSONObject tempJson = jarr.getJSONObject(i);
			String empNum=tempJson.optString("empNum");
			String date=tempJson.optString("date");
			String AmountInTime=tempJson.optString("AmountInTime");
			xml+="<Request Action=\"ApproveSome\">"
			+"<OvertimeApproval AmountInTime =\""+AmountInTime+"\" Date=\""+date+"\" >"
			+"<Employee>"
			+"<PersonIdentity PersonNumber=\""+empNum+"\" />"
			+"</Employee>"
			+"</OvertimeApproval>"
			+"</Request>";
		}
		xml+="</Kronos_WFC>";
		api.sendRequest(xml);
//		JSONObject resposeJson = XML.toJSONObject(api.getXmlReply()).getJSONObject("Kronos_WFC").getJSONObject("Response");
		api.close();
	}
	/**
	 * 获取可申请假期时数不进位
	 * @param empNum
	 * @param leaveType
	 * @param startDate
	 * @param startTime
	 * @param endDate
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public JSONObject checkDataNoRound(String empNum,String leaveType,String startDate,String startTime,String endDate,String endTime) throws Exception{
		XMLClient api = new XMLClient();
		api.open(getKronosConfig());
		String xml= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+"<Kronos_WFC Version=\"1.0\">"
				+"<Request Action=\"CheckDataNoRound\">"
				+"<CNLeaveRequest LeaveType=\""+leaveType+"\" StartDate=\""+startDate+"\" StartTime=\""+startTime+"\" EndDate=\""+endDate+"\" EndTime=\""+endTime+"\" TBLName=\"\">"
				+"<Employee>"
				+"<PersonIdentity PersonNumber=\""+empNum+"\" />"
				+"</Employee>"
				+"</CNLeaveRequest>"
				+"</Request>"
				+"</Kronos_WFC>";
		api.sendRequest(xml);
		JSONObject resposeJson = XML.toJSONObject(api.getXmlReply()).getJSONObject("Kronos_WFC").getJSONObject("Response");
		api.close();
		return resposeJson;
	}
	/**
	 * 获取可申请假期天数
	 * @param empNum
	 * @param leaveType
	 * @param startDate 
	 * @param startTime
	 * @param endDate
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public JSONObject checkDataRound(String empNum,String leaveType,String startDate,String startTime,String endDate,String endTime) throws Exception{
		XMLClient api = new XMLClient();
		api.open(getKronosConfig());
		String xml= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+"<Kronos_WFC Version=\"1.0\">"
				+"<Request Action=\"CheckDataRound\">"
				+"<CNLeaveRequest LeaveType=\""+leaveType+"\" StartDate=\""+startDate+"\" StartTime=\""+startTime+"\" EndDate=\""+endDate+"\" EndTime=\""+endTime+"\" TBLName=\"\">"
				+"<Employee>"
				+"<PersonIdentity PersonNumber=\""+empNum+"\" />"
				+"</Employee>"
				+"</CNLeaveRequest>"
				+"</Request>"
				+"</Kronos_WFC>";
		api.sendRequest(xml);
		JSONObject resposeJson = XML.toJSONObject(api.getXmlReply()).getJSONObject("Kronos_WFC").getJSONObject("Response");
		api.close();
		return resposeJson;
	}
	
	/**
	 * 哺乳假写入
	 * @param empNum
	 * @param startDate
	 * @param startTime
	 * @param endDate
	 * @param endTime
	 * @param startOrEnd
	 * @return
	 * @throws Exception
	 */
	public JSONObject update(String empNum,String startDate,String startTime,String endDate,String endTime,String startOrEnd) throws Exception{
		XMLClient api = new XMLClient();
		api.open(getKronosConfig());
		String xml= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+"<Kronos_WFC Version=\"1.0\">"
				+"<Request Action=\"Update\">"
				+"<CNLeaveRequest LeaveType=\"103\" StartDate=\""+startDate+"\" StartTime=\""+startTime+"\" EndDate=\""+endDate+"\" EndTime=\""+endTime+"\" TBLName=\"\" StartOrEnd=\""+startOrEnd+"\" >"
				+"<Employee>"
				+"<PersonIdentity PersonNumber=\""+empNum+"\" />"
				+"</Employee>"
				+"</CNLeaveRequest>"
				+"</Request>"
				+"</Kronos_WFC>";
		api.sendRequest(xml);
		JSONObject resposeJson = XML.toJSONObject(api.getXmlReply()).getJSONObject("Kronos_WFC").getJSONObject("Response");
		api.close();
		return resposeJson;
	}
	public JSONObject AddOnly(String empNum,String startDate,String startTime,String endDate,String endTime,String startOrEnd) throws Exception{
		XMLClient api = new XMLClient();
		api.open(getKronosConfig());
		String xml= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+"<Kronos_WFC Version=\"1.0\">"
					+"<Request Action=\"AddOnly\">"
				    +"<PayCodeEdit AmountInTimeOrCurrency=\"工资代码时数\" Date=\"开始日期\" PayCodeName=\"工资代码名称\" StartTime =\"开始时间\">"
				    +"<Employee>"
				    +"<PersonIdentity PersonNumber=\"员工号\" />"
				    +"</Employee>"
				    +"</PayCodeEdit>"
				    +"</Request>"
				    +"</Kronos_WFC>";

		api.sendRequest(xml);
		JSONObject resposeJson = XML.toJSONObject(api.getXmlReply()).getJSONObject("Kronos_WFC").getJSONObject("Response");
		api.close();
		return resposeJson;
	}
	public static void main(String[] args) {
		try {
			JSONObject json = new KronosService().getSigOffDay("60001347");
			//System.out.println(json);
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
	private Map<String,Object> getKronosConfig(){
		DatApplication dat = datApplicationService.getDatApplicationByName("SystemMG");
		Map<String, Object> config =getDocumentByField(dat.getAppId(), "SAPConfig", "SearchKey","KronosServer");
		return config;
	}
	
public Map<String, Object> getDocumentByField(String appId,String formName,String fieldName,String fieldValue) {
		
		Map<String, Object> results = new HashMap<String, Object>();
		String sql = "select * from DAT_DOCUMENT where app_id=? and form_Name=? and EXTRACTVALUE(DOCUMENT_DATA,'//root/"+fieldName+"')=?";
//		List<DatDocument> docList = gdao.queryHQL(hql, formName);
		List params = new ArrayList();
		params.add(appId);
		params.add(formName);
		params.add(fieldValue);
		List<DatDocument> docList = gdao.executeJDBCSqlQuery(sql, DatDocument.class, params);
		DatDocument doc = null;
		if (docList.isEmpty()) {
			LOG.warn("在应用库中没有文档！");
		}else{
			doc = docList.get(0);
		}
		if (doc != null) {
			String xmldata = XmlDataUtils.toString(doc.getDocumentData());
			JSONObject jsoDocData = XML.toJSONObject(xmldata);
			if (jsoDocData.has("root")) {
				JSONObject root = jsoDocData.getJSONObject("root");
				Iterator it = root.keys();
				while (it.hasNext()) {
					String key = (String) it.next();
					Object value = root.get(key);
					Object data = DatDocumentUtil.parseValue(value);
					if (data != null) {
						if (data instanceof ArrayList) {
							results.put(key, ((ArrayList<?>) data).get(0));
						} else {
							results.put(key, data);
						}
					}
				}
			}
		}
		return results;
	}
	
}
