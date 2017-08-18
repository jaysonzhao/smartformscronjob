package com.gzsolartech.schedule.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.apache.http.cookie.Cookie;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.gzsolartech.smartforms.constant.EntitySwitchSignal;
import com.gzsolartech.smartforms.constant.OperationType;
import com.gzsolartech.smartforms.constant.bpm.BpmApiEntityStatus;
import com.gzsolartech.smartforms.dao.GenericDao;
import com.gzsolartech.smartforms.entity.DatDocument;
import com.gzsolartech.smartforms.entity.bpm.BpmArchiveStrategy;
import com.gzsolartech.smartforms.entity.bpm.BpmArchivedInstanceInfo;
import com.gzsolartech.smartforms.entity.bpm.BpmGlobalConfig;
import com.gzsolartech.smartforms.entity.bpm.BpmInstanceInfo;
import com.gzsolartech.smartforms.exentity.HttpReturnStatus;
import com.gzsolartech.smartforms.service.BaseDataService;
import com.gzsolartech.smartforms.service.DatDocumentService;
import com.gzsolartech.smartforms.service.SysOptrLogService;
import com.gzsolartech.smartforms.service.bpm.BpmInstanceInfoService;
import com.gzsolartech.smartforms.utils.bpm.BpmClientUtils;
import com.gzsolartech.smartforms.utils.bpm.BpmInstanceUtils;

/**
 * BPM流程实例归档Service
 * @author sujialin
 *
 */
@Service
public class BpmArchiveService extends BaseDataService {
	private static final Logger LOG = LoggerFactory
			.getLogger(BpmArchiveService.class);
	@Autowired
	private GenericDao gdao;
	@Autowired
	private SysOptrLogService sysOptrLogService;
	@Autowired
	private DatDocumentService datDocumentService;
	@Autowired
	private BpmInstanceInfoService bpmInstanceInfoService;
	
	/**
	 * 获取未归档的流程实例信息
	 * @param procAppIds 流程应用库ID列表
	 * @param bpdIds 流程图ID列表
	 * @return
	 */
	public List<BpmInstanceInfo> listUnarchive(Set<String> procAppIds, Set<String> bpdIds) {
		Criteria crita=gdao.getSession().createCriteria(BpmInstanceInfo.class);
		crita.add(Restrictions.eq("instanceExecState", BpmApiEntityStatus.INSTANCE_FINISHED_EXECSTATE));
		crita.add(Restrictions.or(
				Restrictions.eq("isArchived", EntitySwitchSignal.NO),
				Restrictions.isNull("isArchived")));
		if (!CollectionUtils.isEmpty(procAppIds)) {
			crita.add(Restrictions.in("processAppId", procAppIds));
		}
		if (!CollectionUtils.isEmpty(bpdIds)) {
			crita.add(Restrictions.in("bpdId", bpdIds));
		}
		List<BpmInstanceInfo> instInfoList=crita.list();
		return instInfoList;
	}
	
	/**
	 * 开始进行实例归档
	 * @param bpmcfg BPM全局配置信息
	 * @param cookie 当前登录用户Cookie
	 * @param instUtils BPM流程实例工具类
	 * @param instanceInfo 流程实例文档信息
	 * @param strategy 归档策略
	 */
	public void startArchive(BpmGlobalConfig bpmcfg, Cookie cookie, BpmInstanceUtils instUtils,
			BpmInstanceInfo instanceInfo, BpmArchiveStrategy strategy) {
		String instanceId=instanceInfo.getInstanceId();
		String documentId=instanceInfo.getDocumentId();
		try {
			HttpReturnStatus instStatus=instUtils.getInstance(cookie, instanceId);
			if (!BpmClientUtils.isErrorResult(instStatus)) {
				//判断流程实例状态是否为已完成，只有完成状态的流程实例才能进行归档
				JSONObject jsoMsg=new JSONObject(instStatus.getMsg());
				JSONObject jsoData=jsoMsg.getJSONObject("data");
				String instExecStat=jsoData.optString("executionState", "");
				//TODO:判断归档策略是否要需要关联子流程实例的完成情况
				//TODO:判断当前实例的关联子流程实例是否已完成
				//TODO:到底需要关联几层子流程实例？
				if (BpmApiEntityStatus.INSTANCE_FINISHED_EXECSTATE.equals(instExecStat) && 
						instExecStat.equals(instanceInfo.getInstanceExecState())) {
					//调用BPM API删除流程实例
					HttpReturnStatus delinstStatus=instUtils.deleteInstance(cookie, instanceId);
					if (!BpmClientUtils.isErrorResult(delinstStatus)) {
						//创建流程实例归档详细信息
						BpmArchivedInstanceInfo archiveInstance=new BpmArchivedInstanceInfo();
						archiveInstance.setDocumentId(documentId);
						DatDocument datdoc=datDocumentService.loadWithDatApp(documentId);
						if (datdoc!=null) {
							archiveInstance.setAppId(datdoc.getDatApplication().getAppId());
						}
						archiveInstance.setInstanceId(instanceId);
						archiveInstance.setInstanceDetails(instStatus.getMsg());
						archiveInstance.setCreator(bpmcfg.getBpmAdminName());
						archiveInstance.setCreateTime(new Timestamp(System.currentTimeMillis()));
						gdao.save(archiveInstance);
						
						//更新流程实例文档信息的归档状态
						BpmInstanceInfo dbinstInfo=gdao.findById(BpmInstanceInfo.class, 
								instanceInfo.getDocumentId());
						if (dbinstInfo!=null) {
							dbinstInfo.setIsArchived(EntitySwitchSignal.YES);
							dbinstInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
							dbinstInfo.setUpdateBy(bpmcfg.getBpmAdminName());
							gdao.update(dbinstInfo);
						}
						
						//记录归档日志
						String msg="成功归档流程实例！instanceId="+instanceId+", documentId="+documentId;
						sysOptrLogService.create(OperationType.BPM_INSTANCE_ARCHIVE, msg, 
								bpmcfg.getSchedulerHost(), bpmcfg.getBpmAdminName());
						LOG.info(msg);
					} else {
						String msg="删除流程实例失败！instanceId="+instanceId+", documentId="+documentId;
						msg+="\r\n 详细信息："+delinstStatus.getMsg();
						sysOptrLogService.create(OperationType.BPM_INSTANCE_ARCHIVE, msg, 
								bpmcfg.getSchedulerHost(), bpmcfg.getBpmAdminName());
						LOG.error(msg);
					}
				} else {
					String msg="流程实例不是完成状态，无法进行归档！instanceId="+instanceId+", documentId="+documentId;
					sysOptrLogService.create(OperationType.BPM_INSTANCE_ARCHIVE, msg, 
							bpmcfg.getSchedulerHost(), bpmcfg.getBpmAdminName());
					LOG.error(msg);
				}
			} else {
				String msg="获取流程实例详细信息失败，无法进行归档！instanceId="+instanceId+", documentId="+documentId;
				sysOptrLogService.create(OperationType.BPM_INSTANCE_ARCHIVE, msg, 
						bpmcfg.getSchedulerHost(), bpmcfg.getBpmAdminName());
				LOG.error(msg);
			}
		} catch (Exception ex) {
			LOG.error("归档流程实例的时候发生异常！instanceId="+instanceId+
					", documentId="+documentId, ex);
		}
	}
}
