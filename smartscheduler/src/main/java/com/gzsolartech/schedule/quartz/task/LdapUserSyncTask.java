package com.gzsolartech.schedule.quartz.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Scheduler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.gzsolartech.schedule.service.SysTimgJobService;
import com.gzsolartech.smartforms.entity.SysTimgJob;
import com.gzsolartech.smartforms.ldap.UserSync;

@Component
public class LdapUserSyncTask extends BaseTask {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8174219291116242604L;

	@Override
	public void run(String jobId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startDate = sdf.format(new Date());
		SysTimgJobService sysTimgJobService = (SysTimgJobService) applicationContext
				.getBean("sysTimgJobService");
		SysTimgJob sysTimgJob = sysTimgJobService.getScheduleJob(jobId);
		UserSync userSync = (UserSync) applicationContext
				.getBean("userSync");
		userSync.insert();
		String stopDate = sdf.format(new Date());
		sysTimgJob.setStartRunTime(startDate);
		sysTimgJob.setLastRunTime(stopDate);
		sysTimgJobService.updateSysTimgJob(sysTimgJob);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
	}

}
