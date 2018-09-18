package com.gzsolartech.schedule.quartz.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.gzsolartech.schedule.service.SysTimgJobService;
import com.gzsolartech.smartforms.entity.SysTimgJob;
import com.gzsolartech.smartforms.ldap.UserSync;

@Component
public class LdapUserSyncTask extends BaseTask {
	private static final Logger LOG = LoggerFactory
			.getLogger(LdapUserSyncTask.class);
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
		try {
			userSync.insert();
		} catch (Exception e) {
			LOG.error("同步LDAP用户时发生异常！", e);
		}
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
