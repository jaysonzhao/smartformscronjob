package com.gzsolartech.bpmportal.util.email;

import javax.xml.ws.Holder;

import com.alibaba.fastjson.JSON;
import com.gzsolartech.smartforms.extproperty.EmailNotificationExtProperty;

public class EmailNotificationUtil implements EmailNotificationExtProperty {

	@Override
	public Object execute(String account, String password, String address,
			String title, String content) {
		SendMail sm = new SendMail();
		SendMailSoap soap = sm.getSendMailSoap();
		//System.out.println(soap.test());
		Holder<Boolean> sendResult = new Holder<Boolean>();
		Holder<String> errmsg = new Holder<String>();
		soap.sendMailForEIP("BPMServer@aactechnologies.com", address, title,
				content, account, password, "", "", sendResult, errmsg);
		//System.out.println("收件地址:"+address);
		//System.out.println("标题:"+title);
		//System.out.println("内容:"+content);
		//System.out.println("调用接口发送邮件result:"+JSON.toJSONString(sendResult));
		System.out.println("调用接口发送邮件result:"+sendResult.value+", errmsg:"+errmsg.value);
		return null;
	}

	public Object execute(String account, String password, String address,
			String title, String content,String copyTo) {
		SendMail sm = new SendMail();
		SendMailSoap soap = sm.getSendMailSoap();
		//System.out.println(soap.test());
		Holder<Boolean> sendResult = new Holder<Boolean>();
		Holder<String> errmsg = new Holder<String>();
		soap.sendMailForEIP("BPMServer@aactechnologies.com", address, title,
				content, account, password, copyTo, "", sendResult, errmsg);
		System.out.println("抄送:调用接口发送邮件result:"+sendResult.value+", errmsg:"+errmsg.value);
		return null;
	}
}