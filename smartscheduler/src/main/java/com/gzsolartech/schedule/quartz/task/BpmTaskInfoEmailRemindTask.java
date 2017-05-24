package com.gzsolartech.schedule.quartz.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.gzsolartech.schedule.service.BpmTaskInfoEmailRemindService;
@Component
public class BpmTaskInfoEmailRemindTask extends BaseTask{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BpmTaskInfoEmailRemindTask.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

{

}

@Override
public void setApplicationContext(ApplicationContext applicationContext)
		throws BeansException {
	// TODO Auto-generated method stub
	this.applicationContext=applicationContext;
}

@Override
public void run(String jobId) {
	LOGGER.debug("每日提醒执行开始");
	// TODO Auto-generated method stub
	try{
	BpmTaskInfoEmailRemindService bpmTaskInfoEmailRemindService = (BpmTaskInfoEmailRemindService) applicationContext
			.getBean("bpmTaskInfoEmailRemindService");
	bpmTaskInfoEmailRemindService.execute();
	}catch(Exception e){
		LOGGER.debug("每日提醒执行异常", e);
	}
	LOGGER.debug("每日提醒执行结束");
}
}
