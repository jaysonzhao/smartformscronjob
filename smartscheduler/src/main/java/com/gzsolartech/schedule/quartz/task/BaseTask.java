package com.gzsolartech.schedule.quartz.task;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.gzsolartech.smartforms.constant.SysConfigurationTypeName;
import com.gzsolartech.smartforms.entity.SysTimgJob;
import com.gzsolartech.smartforms.service.SysConfigurationService;
import com.gzsolartech.smartforms.utils.MailUtil;

public abstract class BaseTask implements Serializable, ApplicationContextAware {
	private static final Logger LOG = LoggerFactory
			.getLogger(BaseTask.class);
	private static final long serialVersionUID = 1L;
	public static ApplicationContext applicationContext;

	public abstract void run(String jobId);

	/**
	 * 
	 * @Title: senEmail
	 * @Description: 发送邮件
	 * @return void 返回类型
	 * @throws
	 */
	public void senEmail(SysTimgJob sysTimgJob, String content) {
		SysConfigurationService sysConfigurationService = (SysConfigurationService) applicationContext
				.getBean("sysConfigurationService");
		Map<String, Object> config = sysConfigurationService
				.getSysConfiguration(SysConfigurationTypeName.SYSTEM_CONFIG);
		String email = sysTimgJob.getNotifyEmail();
		if (StringUtils.isNotBlank(email)
				&& StringUtils.isNotBlank(sysTimgJob.getErrorNotify())
				&& sysTimgJob.getErrorNotify().equals("enabled")) {
			String[] emails = email.split(",");
			for (String em : emails) {
				String result = MailUtil.sendMail(config.get("SMTPAddress")
						.toString(), config.get("port").toString(),
						config.get("email").toString(), config.get("account")
								.toString(), config.get("password").toString(),
						em.trim(), "数据调度任务采集异常", content, true, false);
				LOG.info(result);
			}
		}

	}
}