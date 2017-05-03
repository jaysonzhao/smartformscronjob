package com.gzsolartech.schedule.quartz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.gzsolartech.schedule.service.SysTimgJobService;
import com.gzsolartech.smartforms.entity.SysTimgJob;
@Service
public class TaskServiceManager {
	private static final Logger logger = LoggerFactory
			.getLogger(TaskServiceManager.class);

	private SchedulerFactoryBean schedulerFactoryBean;
	@Autowired
	private Scheduler scheduler;
	@Autowired
	private GetJobs getJobs;
	@Autowired
	private SysTimgJobService scheduleJobService;

	/**
	 * 停止一个任务
	 * 
	 * @param scheduleJob
	 * @throws Exception
	 */
	public void pauseJob(String jodId) throws Exception {
		SysTimgJob scheduleJob = scheduleJobService.getScheduleJob(jodId);
		if (scheduleJob == null || !scheduleJob.getJobStatus().equals("1")) {
			throw new Exception("任务未启动");
		} else {
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobId(),
					scheduleJob.getJobGroup());
			scheduler.pauseJob(jobKey);
			scheduleJob.setJobStatus("3");
			scheduleJobService.updateSysTimgJob(scheduleJob);
		}
	}

	/**
	 * 恢复一个任务
	 * 
	 * @param scheduleJob
	 * @throws Exception
	 */
	public void resumeJob(String jobId) throws Exception {
		SysTimgJob scheduleJob = scheduleJobService.getScheduleJob(jobId);
		if (scheduleJob == null || !scheduleJob.getJobStatus().equals("3")) {
			throw new Exception("没有暂停的任务");
		} else {
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobId(),
					scheduleJob.getJobGroup());
			scheduler.resumeJob(jobKey);
			scheduleJob.setJobStatus("1");
			scheduleJobService.updateSysTimgJob(scheduleJob);
		}
	}

	/**
	 * 删除一个任务
	 * 
	 * @param scheduleJob
	 * @throws Exception
	 */
	public void deleteJob(String jobId) throws Exception {
		SysTimgJob scheduleJob = scheduleJobService.getScheduleJob(jobId);
		if (scheduleJob == null) {
			throw new Exception("没有启动的任务");
		} else {
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobId(),
					scheduleJob.getJobGroup());
			scheduler.deleteJob(jobKey);
			scheduleJob.setJobStatus("2");
			scheduleJobService.updateSysTimgJob(scheduleJob);

		}
	}

	/**
	 * 立即执行任务
	 * 
	 * @param scheduleJob
	 * @throws Exception
	 */
	public void triggerJob(String jobId) throws Exception {
		SysTimgJob scheduleJob = scheduleJobService.getScheduleJob(jobId);
		if (scheduleJob == null) {
			throw new Exception("无该任务");
		} else {
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobId(),
					scheduleJob.getJobGroup());
			scheduler.triggerJob(jobKey);
			scheduleJob.setJobStatus("1");
			// scheduleJobService.updateSysTimgJob(scheduleJob);
		}
	}

	/**
	 * 更新任务的时间表达式
	 * 
	 * @param scheduleJob
	 * @throws Exception
	 */
	public void rescheduleJob(String jobId, String cronExpression)
			throws Exception {
		SysTimgJob scheduleJob = scheduleJobService.getScheduleJob(jobId);
		if (scheduleJob == null) {
			throw new Exception("任务未启动");
		} else {
			scheduleJob.setCronExpression(cronExpression);
			scheduleJobService.updateSysTimgJob(scheduleJob);
			TriggerKey triggerKey = TriggerKey.triggerKey(
					scheduleJob.getJobId(), scheduleJob.getJobGroup());
			// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
			CronTrigger trigger = (CronTrigger) scheduler
					.getTrigger(triggerKey);
			if (null != trigger) {
				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
						.cronSchedule(scheduleJob.getCronExpression());

				// 按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
						.withSchedule(scheduleBuilder).build();

				// 按新的trigger重新设置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
				scheduleJob.setJobStatus("1");
				scheduleJobService.updateSysTimgJob(scheduleJob);
			} else {
				throw new Exception("任务未启动");
			}
		}

	}

	/**
	 * 启动一个任务
	 * 
	 * @param job
	 * @throws Exception
	 */
	public void scheduleJob(String jobId) throws Exception {
		SysTimgJob scheduleJob = scheduleJobService.getScheduleJob(jobId);
		if (scheduleJob == null) {
			throw new Exception("无该任务");
		}
		scheduleJob.setJobStatus("1");
		TaskJob taskJob = new TaskJob();
		taskJob.setJobId(scheduleJob.getJobId());
		taskJob.setJobName(scheduleJob.getJobName());
		taskJob.setJobGroup(scheduleJob.getJobGroup());
		taskJob.setCronExpression(scheduleJob.getCronExpression());
		taskJob.setJobStatus(scheduleJob.getJobStatus());
		taskJob.setDesc(scheduleJob.getDescription());
		String schedulerName = scheduleJob.getSchedulerName();
		if (StringUtils.isBlank(schedulerName)) {
			throw new Exception("请选择一个调度任务");
		}
		taskJob.setBaseTask(getJobs.job(schedulerName));
		TriggerKey triggerKey = TriggerKey.triggerKey(taskJob.getJobId(),
				taskJob.getJobGroup());
		// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		// 不存在，创建一个
		if (trigger == null) {
			JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class)
					.withIdentity(taskJob.getJobId(), taskJob.getJobGroup())
					.build();
			jobDetail.getJobDataMap().put("scheduleJob", taskJob);
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
					.cronSchedule(taskJob.getCronExpression());
			// 按新的cronExpression表达式构建一个新的trigger
			trigger = TriggerBuilder.newTrigger()
					.withIdentity(taskJob.getJobId(), taskJob.getJobGroup())
					.withSchedule(scheduleBuilder).build();
			scheduler.scheduleJob(jobDetail, trigger);

		} else {
			// Trigger已存在，那么更新相应的定时设置
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
					.cronSchedule(taskJob.getCronExpression());
			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
					.withSchedule(scheduleBuilder).build();
			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		}
		scheduleJob.setJobStatus("1");
		scheduleJobService.updateSysTimgJob(scheduleJob);
	}

	/**
	 * 启动所有的任务
	 * 
	 * @param jobsId
	 * @return
	 */
	public Map<String, Object> scheduleJobs(String[] jobsId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "");
		for (String jobId : jobsId) {
			try {
				scheduleJob(jobId);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("success", false);
				map.put("msg", map.get("msg") + e.getMessage());
			}
		}
		return map;
	}

	/**
	 * 删除所有的任务
	 * 
	 * @param jobsId
	 * @return
	 */
	public Map<String, Object> deleteJobs(String[] jobsId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "");
		for (String jobId : jobsId) {
			try {
				deleteJob(jobId);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				map.put("success", false);
				map.put("msg", map.get("msg") + e.getMessage());
			}
		}
		return map;
	}

	/**
	 * 暂停所有任务
	 * 
	 * @param jobsId
	 * @return
	 */
	public Map<String, Object> pauseJobs(String[] jobsId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "");
		for (String jobId : jobsId) {
			try {
				pauseJob(jobId);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("success", false);
				map.put("msg", map.get("msg") + e.getMessage());
			}
		}
		return map;
	}

	/**
	 * 立即执行所有的任务
	 * 
	 * @param jobsId
	 * @return
	 */
	public Map<String, Object> triggerJobs(String[] jobsId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "");
		for (String jobId : jobsId) {
			try {
				triggerJob(jobId);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("success", false);
				map.put("msg", map.get("msg") + e.getMessage());
			}
		}
		return map;
	}

	public Map<String, Object> resumeJobs(String[] jobsId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", "");
		for (String jobId : jobsId) {
			try {
				resumeJob(jobId);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("success", false);
				map.put("msg", map.get("msg") + e.getMessage());
			}
		}
		return map;
	}

	/**
	 * 执行配置的所有任务
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception {
		/*
		 * List<ScheduleJob> jobList =dao.getAll(ScheduleJob.class); for
		 * (TaskJob job : jobList) { TriggerKey triggerKey =
		 * TriggerKey.triggerKey(job.getJobName(), job.getJobGroup()); //
		 * 获取trigger，即在spring配置文件中定义的 bean id="myTrigger" CronTrigger trigger =
		 * (CronTrigger) scheduler .getTrigger(triggerKey); // 不存在，创建一个 if (null
		 * == trigger) { JobDetail jobDetail =
		 * JobBuilder.newJob(QuartzJobFactory.class)
		 * .withIdentity(job.getJobName(), job.getJobGroup()) .build();
		 * jobDetail.getJobDataMap().put("scheduleJob", job);
		 * 
		 * // 表达式调度构建器 CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
		 * .cronSchedule(job.getCronExpression());
		 * 
		 * // 按新的cronExpression表达式构建一个新的trigger trigger =
		 * TriggerBuilder.newTrigger() .withIdentity(job.getJobName(),
		 * job.getJobGroup()) .withSchedule(scheduleBuilder).build();
		 * 
		 * scheduler.scheduleJob(jobDetail, trigger); } else { //
		 * Trigger已存在，那么更新相应的定时设置 // 表达式调度构建器 CronScheduleBuilder
		 * scheduleBuilder = CronScheduleBuilder
		 * .cronSchedule(job.getCronExpression());
		 * 
		 * // 按新的cronExpression表达式重新构建trigger trigger =
		 * trigger.getTriggerBuilder().withIdentity(triggerKey)
		 * .withSchedule(scheduleBuilder).build();
		 * 
		 * // 按新的trigger重新设置job执行 scheduler.rescheduleJob(triggerKey, trigger);
		 * } }
		 */
	}

	/**
	 * 获取所的任务列表
	 * 
	 * @return
	 */
	public List<SysTimgJob> getAllScheduleJob() {
		List<SysTimgJob> scheduleJob = scheduleJobService.getAll();
		return scheduleJob;
	}

	public SchedulerFactoryBean getSchedulerFactoryBean() {
		return schedulerFactoryBean;
	}

	public void setSchedulerFactoryBean(
			SchedulerFactoryBean schedulerFactoryBean) {
		this.schedulerFactoryBean = schedulerFactoryBean;
	}

}
