package com.gzsolartech.schedule.quartz.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.cookie.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.gzsolartech.schedule.service.BpmArchiveService;
import com.gzsolartech.smartforms.constant.EntitySwitchSignal;
import com.gzsolartech.smartforms.entity.bpm.BpmArchiveStrategy;
import com.gzsolartech.smartforms.entity.bpm.BpmArchiveStrategyAssign;
import com.gzsolartech.smartforms.entity.bpm.BpmGlobalConfig;
import com.gzsolartech.smartforms.entity.bpm.BpmInstanceInfo;
import com.gzsolartech.smartforms.entity.bpm.BpmProcessMeta;
import com.gzsolartech.smartforms.exentity.HttpReturnStatus;
import com.gzsolartech.smartforms.service.bpm.BpmArchiveStrategyService;
import com.gzsolartech.smartforms.service.bpm.BpmGlobalConfigService;
import com.gzsolartech.smartforms.service.bpm.BpmProcessMetaService;
import com.gzsolartech.smartforms.utils.bpm.BpmClientUtils;
import com.gzsolartech.smartforms.utils.bpm.BpmInstanceUtils;

/**
 * 流程实例归档调度任务类
 * @author sujialin
 *
 */
@Component
public class BpmArchiveInstanceTask extends BaseTask {
	private static final Logger LOG = LoggerFactory
			.getLogger(BpmArchiveInstanceTask.class);

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext=applicationContext;
	}

	@Override
	public void run(String jobId) {
		BpmArchiveStrategyService bpmArchiveStrategyService=(BpmArchiveStrategyService)
				applicationContext.getBean(BpmArchiveStrategyService.class);
		BpmArchiveStrategy strategy=bpmArchiveStrategyService.loadByJobId(jobId, true);
		if (strategy!=null) {
			if (EntitySwitchSignal.YES.equals(strategy.getIsEnabled())) {
				Set<BpmArchiveStrategyAssign> assignSet=strategy.getBpmArchiveStrategyAssignSet();
				if (!CollectionUtils.isEmpty(assignSet)) {
					//获取策略中要归档的流程
					Set<String> workflowIds=new HashSet<String>();
					for (BpmArchiveStrategyAssign assign : assignSet) {
						String wfid=assign.getWorkflowId();
						if (StringUtils.isNotBlank(wfid)) {
							workflowIds.add(wfid);
						}
					}
					//获取要进行归档的流程元数据
//					List<BpmProcessMeta> procmetaList=new ArrayList<BpmProcessMeta>();
					Set<String> procAppIds=new HashSet<String>();
					Set<String> bpdIds=new HashSet<String>();
					for (String wkid : workflowIds) {
						BpmProcessMetaService bpmProcessMetaService=applicationContext
								.getBean(BpmProcessMetaService.class);
						BpmProcessMeta procmeta=bpmProcessMetaService.getById(wkid);
						if (procmeta!=null) {
//							procmetaList.add(procmeta);
							procAppIds.add(procmeta.getProcessAppId());
							bpdIds.add(procmeta.getBpdId());
						}
					}
					
					//获取所有待归档的流程实例
					BpmArchiveService bpmArchiveService=applicationContext.getBean(BpmArchiveService.class);
					List<BpmInstanceInfo> instInfos=bpmArchiveService.listUnarchive(procAppIds, bpdIds);
					//使用BPM管理员身份进行登录，获取Cookie信息
					BpmGlobalConfigService bpmGlobalConfigService=applicationContext
							.getBean(BpmGlobalConfigService.class);
					BpmGlobalConfig bpmcfg=bpmGlobalConfigService.getFirstActConfig();
					BpmClientUtils bpmUtils=new BpmClientUtils(bpmcfg, true);
					List<Cookie> cookies=new ArrayList<Cookie>();
					HttpReturnStatus loginStatus=bpmUtils.doLogin(bpmcfg.getBpmAdminName(), 
							bpmcfg.getBpmAdminPsw(), cookies);
					if (CollectionUtils.isEmpty(cookies)) {
						LOG.error("登录失败，没有获取到身份Cookie！loginMsg="+loginStatus.getMsg());
						return;
					}
					BpmInstanceUtils instUtils=new BpmInstanceUtils(bpmcfg, true);
					try {
						//逐个获取流程实例详细信息
						for (BpmInstanceInfo instInfo : instInfos) {
							bpmArchiveService.startArchive(bpmcfg, cookies.get(0), instUtils, 
									instInfo, strategy);
						}
					} catch (Exception ex) {
						LOG.error("进行流程实例归档时发生异常！", ex);
					} finally {
						instUtils.closeClient();
					}
				}
			} else {
				LOG.info("归档策略已关闭，无需进行归档！strategyId="+strategy.getStrategyId()+
						", strategyName="+strategy.getStrategyName());
			}
		} else {
			LOG.error("找不到归档策略！jobId="+jobId);
		}
	}

}
