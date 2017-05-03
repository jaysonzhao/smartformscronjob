package com.gzsolartech.schedule.quartz.task;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.gzsolartech.smartforms.service.bpm.BpmEmailNotificationService;
@Component
public class BpmActivityEmailNotificationTask extends BaseTask{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext=applicationContext;
	}

	@Override
	public void run(String jobId) {
		// TODO Auto-generated method stub
		BpmEmailNotificationService bpmEmailNotificationService = (BpmEmailNotificationService) applicationContext
				.getBean("bpmEmailNotificationService");
		bpmEmailNotificationService.startActivityNotification();
	}

}
