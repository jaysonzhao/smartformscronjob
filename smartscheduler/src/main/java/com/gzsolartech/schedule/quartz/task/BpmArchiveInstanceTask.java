package com.gzsolartech.schedule.quartz.task;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BpmArchiveInstanceTask extends BaseTask {

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.applicationContext=applicationContext;
	}

	@Override
	public void run(String jobId) {
		// TODO Auto-generated method stub

	}

}
