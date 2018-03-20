package com.gzsolartech.schedule.quartz.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.gzsolartech.smartforms.service.bpm.BpmInstanceSyncSignalService;

/**
 * 定时同步BPM流程实例状态
 * @author sujialin
 *
 */
@Component
public class BpmInstanceSyncSignalTask extends BaseTask {
	private static final Logger LOG = LoggerFactory
			.getLogger(BpmInstanceSyncSignalTask.class);
	
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.applicationContext=applicationContext;
	}

	@Override
	public void run(String jobId) {
		BpmInstanceSyncSignalService bpmInstanceSyncSignalService=applicationContext
				.getBean(BpmInstanceSyncSignalService.class);
		try {
			//同步BPM实例状态
			LOG.debug("同步BPM实例状态...");
			bpmInstanceSyncSignalService.updateSyncInstance(true);
		} catch (Exception ex) {
			LOG.error("同步BPM流程实例状态信号线程发生异常！", ex);
		}
	}

}
