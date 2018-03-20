package com.gzsolartech.schedule.quartz;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.gzsolartech.schedule.quartz.task.BaseTask;


@Component
public class GetJobs implements ApplicationContextAware {
	private static ApplicationContext applicationContext; // Spring应用上下文环境

	public BaseTask job(String beanId) {
		return (BaseTask) applicationContext.getBean(beanId);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		GetJobs.applicationContext = applicationContext;
	}
}
