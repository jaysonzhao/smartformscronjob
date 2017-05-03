package com.gzsolartech.schedule.quartz;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;

public class ApplicationContextListener implements ServletContextListener {
	private Logger logger = Logger.getLogger(this.getClass());

	public static Scheduler scheduler = null;

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		this.logger.info("The application start...");
		/* 注册定时任务 */
		try { // 获取Scheduler实例
			/*
			 * scheduler = QuartzManage.getScheduler(); scheduler.start();
			 * scheduler.resumeAll();//唤醒暂停的任务
			 */} catch (Exception se) {
			logger.error(se.getMessage(), se);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		this.logger.info("The application stop...");
		/* 注销定时任务 */
		try {
			// 关闭Scheduler
			// scheduler.shutdown();
			// this.logger.info("The scheduler shutdown...");
		} catch (Exception se) {
			logger.error(se.getMessage(), se);
		}
	}

}
