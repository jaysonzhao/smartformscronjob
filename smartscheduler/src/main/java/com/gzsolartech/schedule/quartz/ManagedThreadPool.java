package com.gzsolartech.schedule.quartz;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.quartz.SchedulerConfigException;
import org.quartz.spi.ThreadPool;

import commonj.work.WorkException;
import commonj.work.WorkManager;

public class ManagedThreadPool implements ThreadPool {
	private String workManagerName;
	private WorkManager workManager;
	
	@Override
	public boolean runInThread(Runnable runnable) {
		try {
			workManager.schedule(new WorkAdapter(runnable));
			
			return true;
		} catch (IllegalArgumentException e) {
			// Todo: log error
			e.printStackTrace();
			return false;
		} catch (WorkException e) {
			// Todo: loge
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int blockForAvailableThreads() {
		return 1;
	}

	@Override
	public void initialize() throws SchedulerConfigException {
		try {
			workManager = InitialContext.doLookup(workManagerName);
		} catch (NamingException e) {
			throw new IllegalStateException("Could not locate WorkManager: " + e.getMessage(), e);
		}

	}

	@Override
	public void shutdown(boolean waitForJobsToComplete) {
	}

	@Override
	public int getPoolSize() {
		return -1;
	}
	public void setThreadCount(int threadCount){
		
	}

	@Override
	public void setInstanceId(String schedInstId) {
	}

	@Override
	public void setInstanceName(String schedName) {
	}

	public void setWorkManagerName(String workManagerName) {
		this.workManagerName = workManagerName;
	}

}
