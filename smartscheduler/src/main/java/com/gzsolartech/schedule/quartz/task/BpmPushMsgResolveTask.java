package com.gzsolartech.schedule.quartz.task;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.gzsolartech.smartforms.entity.bpm.BpmGlobalConfig;
import com.gzsolartech.smartforms.entity.bpm.BpmOriginalPushMsg;
import com.gzsolartech.smartforms.exentity.HttpReturnStatus;
import com.gzsolartech.smartforms.service.bpm.BpmGlobalConfigService;
import com.gzsolartech.smartforms.service.bpm.BpmOriginalPushMsgService;
import com.gzsolartech.smartforms.utils.HttpClientUtils;

/**
 * 重试解析未处理的BPM推送消息定时任务
 * @author sujialin
 *
 */
@Component
public class BpmPushMsgResolveTask extends BaseTask {
	private static final Logger LOG = LoggerFactory
			.getLogger(BpmPushMsgResolveTask.class);
	
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.applicationContext=applicationContext;
	}

	@Override
	public void run(String jobId) {
		//获取创建了2分钟但是还没有解析的推送消息
		BpmOriginalPushMsgService bpmOriginalPushMsgService=applicationContext
				.getBean(BpmOriginalPushMsgService.class);
		BpmGlobalConfigService bpmGlobalConfigService=applicationContext
				.getBean(BpmGlobalConfigService.class);
		try {
			BpmGlobalConfig bpmcfg=bpmGlobalConfigService.getFirstActConfig();
			List<BpmOriginalPushMsg> pushmsgList=bpmOriginalPushMsgService
					.listByUnprocess();
			if (!CollectionUtils.isEmpty(pushmsgList)) {
				//调用解析推送消息的REST API接口
				String formhost=bpmcfg.getBpmformsHost();
				formhost = formhost.endsWith("/") ? 
						formhost.substring(0, formhost.length()-1) : formhost;
				String formctx=bpmcfg.getBpmformsWebContext();
				if (StringUtils.isBlank(formctx)) {
					formctx="";
				} else 	if (!formctx.startsWith("/")) {
					formctx="/"+formctx;
				}
				String url=formhost+formctx;
				url=url.endsWith("/") ? url : url+"/";
				url+="console/bpm/taskInfo/pullOrigMsg.xsp";
				LOG.debug("pushmsg url="+url);
				
				//判断消息创建日期是否在2017-12-14日之后，在这个日期之前的消息都不进行处理了，避免产生更多的问题
				Calendar cale=Calendar.getInstance();
				cale.set(Calendar.YEAR, 2017);
				cale.set(Calendar.MONTH, Calendar.DECEMBER);
				cale.set(Calendar.DAY_OF_MONTH, 14);
				cale.set(Calendar.HOUR_OF_DAY, 1);
				cale.set(Calendar.MINUTE, 1);
				cale.set(Calendar.SECOND, 1);
				Timestamp timeline=new Timestamp(cale.getTimeInMillis());
				Timestamp tsnow=new Timestamp(System.currentTimeMillis());
				//循环遍历未处理消息列表
				for (BpmOriginalPushMsg pushMsg : pushmsgList) {
					Timestamp tscreate=pushMsg.getCreateTime();
					if (tscreate.after(timeline)) {
						//另外判断创建时间是否已经超过3分钟，若超过3分钟还未处理，就进行重试解析
						Calendar calCreate=Calendar.getInstance();
						calCreate.setTimeInMillis(tscreate.getTime());
						calCreate.add(Calendar.MINUTE, 3);
						Timestamp tsover3=new Timestamp(calCreate.getTimeInMillis());
						if (tsover3.after(tsnow)) {
							continue;
						}
						Map<String, Object> params=new HashMap<>();
						params.put("msgId", pushMsg.getMsgId());
						HttpReturnStatus httpStatus=HttpClientUtils.doGet(url, params);
						LOG.debug("重试解析BPM推送消息，msgId="+pushMsg.getMsgId()
								+", returnMsg="+httpStatus.getMsg());
					}
				}
			}
		} catch (Exception ex) {
			LOG.error("执行推送消息推送解析时发生异常！", ex);
		}
	}
}
