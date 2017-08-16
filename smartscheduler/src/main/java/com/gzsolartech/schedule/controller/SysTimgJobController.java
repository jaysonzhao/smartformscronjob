package com.gzsolartech.schedule.controller;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.gzsolartech.schedule.quartz.TaskServiceManager;
import com.gzsolartech.schedule.service.SysTimgJobService;
import com.gzsolartech.smartforms.controller.BaseWebController;
import com.gzsolartech.smartforms.entity.SysTimgJob;


/**
 * 
 * @ClassName: SysTimgJobController
 * @Description: 调度任务执行操作
 * @author wwd
 * @date 2016年8月23日 下午2:39:37
 *
 */
@Controller
@RequestMapping("/console/quartz")
public class SysTimgJobController extends BaseWebController {
	public static final Logger LOGGER = LoggerFactory
			.getLogger(SysTimgJobController.class);
	@Autowired
	private TaskServiceManager taskServiceManager;
	@Autowired
	private SysTimgJobService sysTimgJobService;

	/**
	 * 任务管理主页
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("tasks", sysTimgJobService.getTasks());
		mav.setViewName("/console/sysTimgJob/index");
		return mav;
	}

	/**
	 * 获取配置的任务
	 * 
	 * @return
	 */
	@RequestMapping("/getAllScheduleJob")
	@ResponseBody
	public Map<String, Object> getAllModels(int page, int rows) {
		Map<String, Object> dataResult = sysTimgJobService.findByPage(page,
				rows);
		return dataResult;
	}

	/**
	 * 启动任务
	 * 
	 * @param taskNames
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/startTasks")
	public Map<String, Object> startTasks(
			@RequestParam("jobsId[]") String[] jobsId) {
		Map<String, Object> dataMap = taskServiceManager.scheduleJobs(jobsId);
		return dataMap;
	}

	/**
	 * 停止任务
	 * 
	 * @param taskNames
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/stopTasks")
	public Map<String, Object> stopTasks(
			@RequestParam("jobsId[]") String[] jobsId) {
		Map<String, Object> dataMap = taskServiceManager.deleteJobs(jobsId);
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/pauseTasks")
	public Map<String, Object> pauseTasks(
			@RequestParam("jobsId[]") String[] jobsId) {
		Map<String, Object> dataMap = taskServiceManager.pauseJobs(jobsId);
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/triggerTasks")
	public Map<String, Object> triggerTasks(
			@RequestParam("jobsId[]") String[] jobsId) {
		Map<String, Object> dataMap = taskServiceManager.triggerJobs(jobsId);
		return dataMap;
	}

	@ResponseBody
	@RequestMapping("/resumeTasks")
	public Map<String, Object> resumeTasks(
			@RequestParam("jobsId[]") String[] jobsId) {
		Map<String, Object> dataMap = taskServiceManager.resumeJobs(jobsId);
		return dataMap;
	}

	/**
	 * 新建或更新调度任务
	 * 
	 * @Title: saveOrUpdate
	 * @Description:
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@RequestMapping(value="/saveOrUpdate", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveOrUpdate(SysTimgJob sysTimgJob) {
		Map<String, Object> results = new HashMap<String, Object>();
		try {
			sysTimgJob.setCreateTime(new Timestamp(System.currentTimeMillis()));
			sysTimgJob.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			sysTimgJob.setCreator(getEmployeeName());
			sysTimgJob.setUpdateBy(getEmployeeName());
			LOGGER.debug("正在保存调度任务......");
			String jobId=sysTimgJobService.saveOrUpdate(sysTimgJob);
			LOGGER.debug("调度任务保存成功！");
			results.put("state", true);
			results.put("msg", jobId);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			results.put("state", false);
			results.put("msg", e.getMessage());
		}

		return results;
	}

	/**
	 * 
	 * @Title: deletTasks
	 * @Description: 删除调度任务
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value="/deletTasks", method=RequestMethod.POST)
	public Map<String, Object> deletTasks(
			@RequestParam("jobsId[]") String[] jobsId) {
		Map<String, Object> results = new HashMap<String, Object>();
		try {
			// 先停止调度任务进程
			LOGGER.debug("正在删除调度任务......\r\n待删除任务ID："+Arrays.toString(jobsId));
			taskServiceManager.deleteJobs(jobsId);
			LOGGER.debug("完成删除调度任务！");
			// 删除配置的调度任务
			sysTimgJobService.deleteJob(jobsId);
			results.put("success", true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			results.put("success", false);
			results.put("msg", e.getMessage());
		}
		return results;
	}
}
