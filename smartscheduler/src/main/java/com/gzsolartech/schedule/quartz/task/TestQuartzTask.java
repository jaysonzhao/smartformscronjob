package com.gzsolartech.schedule.quartz.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.gzsolartech.smartforms.entity.DatDocument;
@Component
public class TestQuartzTask extends BaseTask {

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run(String jobId) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		System.out.println("执行任务调度"+df.format(new Date()));// new Date()为获取当前系统时间
	DatDocument datDocument=new DatDocument();
	}

}
