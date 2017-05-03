package com.gzsolartech.schedule.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 * 
 * @ClassName: QuartzJobFactory
 * @Description: 定时任务运行工厂类
 * @author wwd
 * @date 2015年10月20日 下午2:21:38 © Copyright 续日科技
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class QuartzJobFactory implements Job {

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		TaskJob scheduleJob = (TaskJob) context.getMergedJobDataMap().get(
				"scheduleJob");
		scheduleJob.getBaseTask().run(scheduleJob.getJobId());
	}
}