package com.gzsolartech.schedule.quartz;

import commonj.work.Work;

public class WorkAdapter implements Work {
	private final Runnable runnable;
	
	public WorkAdapter(Runnable runnable) {
		if (runnable == null) {
			throw new IllegalArgumentException("Argument can not be null");
		}
		this.runnable = runnable;
	}

	@Override
	public void run() {
		runnable.run();
	}

	@Override
	public boolean isDaemon() {
		return true;
	}

	@Override
	public void release() {
		
	}
	
}

