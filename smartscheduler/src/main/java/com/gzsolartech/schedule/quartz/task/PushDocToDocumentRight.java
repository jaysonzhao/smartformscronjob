package com.gzsolartech.schedule.quartz.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.gzsolartech.smartforms.constant.EntitySwitchSignal;
import com.gzsolartech.smartforms.constant.HttpSessionKey;
import com.gzsolartech.smartforms.entity.DetFormBizPanel;
import com.gzsolartech.smartforms.entity.DetFormDefine;
import com.gzsolartech.smartforms.entity.DetFormPanelBizctrl;
import com.gzsolartech.smartforms.ldap.UserSync;
import com.gzsolartech.smartforms.service.DetFormPanelBizctrlService;

/**
 * 调度任务，将有权限查看区块的人写入读者域
 * @author Lolipop
 *
 */
@Component
public class PushDocToDocumentRight  extends BaseTask  {

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext=applicationContext;
	}

	@Override
	public void run(String jobId) {
		//调用业务区块service类，将有查看权限的人加进读者域
		DetFormPanelBizctrlService bizService = (DetFormPanelBizctrlService) applicationContext
				.getBean("detFormPanelBizctrlService");
		bizService.saveDoc2ReadRight();
	}
}
