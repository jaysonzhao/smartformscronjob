package com.gzsolartech.schedule.quartz;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.quartz.spi.ThreadExecutor;

import commonj.work.WorkException;
import commonj.work.WorkManager;

public class ManagedThreadExecutor implements ThreadExecutor {

	private String workManagerName;
	private WorkManager workManager;

	public void execute(Thread thread) {
		try {
			workManager.schedule(new WorkAdapter(thread));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (WorkException e) {
			e.printStackTrace();
		}
	}

	public void initialize() {
		try {
			workManager = InitialContext.doLookup(workManagerName);
		} catch (NamingException e) {
			throw new IllegalStateException("Could not locate WorkManager: " + e.getMessage(), e);
		}
	}

	public void setWorkManagerName(String workManagerName) {
		this.workManagerName = workManagerName;
	}

}