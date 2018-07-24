package com.gzsolartech.schedule.quartz.task;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.gzsolartech.smartforms.service.DetFormPanelBizctrlService;

/**
 * 调度任务，将有权限查看区块的人写入读者域
 * @author Lolipop
 * 
 * 代码已过时，好久没有人用过了，不知道还能不能用。
 *
 */
@Component
@Deprecated
public class PushDocToDocumentRight  extends BaseTask  {

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext=applicationContext;
	}

	@Override
	public void run(String jobId) {
		//调用业务区块service类，将有查看权限的人加进读者域
		DetFormPanelBizctrlService bizService = (DetFormPanelBizctrlService) applicationContext
				.getBean("detFormPanelBizctrlService");
		bizService.saveDoc2ReadRight();
	}
}
