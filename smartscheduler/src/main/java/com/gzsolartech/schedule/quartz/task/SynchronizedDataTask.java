package com.gzsolartech.schedule.quartz.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.gzsolartech.bpmportal.service.SynchronizedDataService;
import com.gzsolartech.smartforms.exceptions.SmartformsException;
/**
 * @description 数据同步 
 * @author hhf
 * @date 2017年5月25日 上午10:29:36
 */
@Component
public class SynchronizedDataTask extends BaseTask{
	
	private static final long serialVersionUID = 4690002209618844165L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SynchronizedDataTask.class);

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext=applicationContext;
	}

	@Override
	public void run(String jobId) {
		SynchronizedDataService synchronizedDataService = 
				applicationContext.getBean(SynchronizedDataService.class);
		try {
			synchronizedDataService.execute();
		} catch (SmartformsException e) {
			LOGGER.error("同步数据时出现异常：",e);
			e.printStackTrace();
		}
		LOGGER.info("数据同步完成："+new Date());
	}
}
