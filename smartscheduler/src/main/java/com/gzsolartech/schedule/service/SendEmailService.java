package com.gzsolartech.schedule.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzsolartech.bpmportal.util.email.EmailNotificationUtil;
import com.gzsolartech.smartforms.constant.SysConfigurationTypeName;
import com.gzsolartech.smartforms.service.BaseDataService;
import com.gzsolartech.smartforms.service.SysConfigurationService;
import com.gzsolartech.smartforms.utils.MailUtil;
@Service
public class SendEmailService extends BaseDataService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SendEmailService.class);
	@Autowired
	private SysConfigurationService sysConfigurationService;
	/**
	 * 发送邮件
	 *
	 * @param recipientTmp
	 * @param titleTmp
	 * @param contentTmp
	 * @param copyTo
	 * @return void 返回类型
	 * @throws
	 */
	public void sendEmail(String recipientTmp, String titleTmp,
			String contentTmp, String copyTo) {
		//copyTo="sujl@gzsolartech.com";
		//if("wuwd@gzsolartech.com".equals(recipientTmp)){
		//	recipientTmp="GeXiao@aactechnologies.com";
			//recipientTmp="zhuhuahu@aactechnologies.com";	
		LOGGER.debug("发送邮件："+recipientTmp);
		if (StringUtils.isNotBlank(recipientTmp)) {
			Map<String, Object> config = sysConfigurationService
					.getSysConfiguration(SysConfigurationTypeName.SYSTEM_CONFIG);
			String SMTPAddress = config.get("SMTPAddress") + "";
			String port = config.get("port") + "";
			String email = config.get("email") + "";
			String account = config.get("account") + "";
			String password = config.get("password") + "";
			String result = "";
			if (StringUtils.isBlank(copyTo)) {
				/*result = MailUtil.sendMail(SMTPAddress, port, email, account,
						password, recipientTmp, titleTmp, contentTmp, true,
						false);*/
				new EmailNotificationUtil().execute(account,
				password,recipientTmp, titleTmp, contentTmp);
			} else {
				/*result = MailUtil.sendMail(SMTPAddress, port, email, account,
						password, recipientTmp, titleTmp, contentTmp, true,
						false, copyTo);*/
				new EmailNotificationUtil().execute(account, password,
				 recipientTmp, titleTmp, contentTmp,copyTo);
			}
			/*JSONObject jsoResult = new JSONObject(result);
			if (!jsoResult.optBoolean("success", false)) {
				LOGGER.error("邮件发送失败" + result);
			}*/
		}
		}
	//}
}
