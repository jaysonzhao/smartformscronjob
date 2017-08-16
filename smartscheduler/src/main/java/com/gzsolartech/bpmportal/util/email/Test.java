
package com.gzsolartech.bpmportal.util.email;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.Holder;

import org.omg.CORBA.BooleanHolder;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "test")
public class Test {
	public static void main(String[] args) {
	/*	SendMail sm=new SendMail();
		SendMailSoap soap=sm.getSendMailSoap();
		//System.out.println(soap.test());
		Holder<Boolean> sendResult=new Holder<Boolean>();
		Holder<String> errmsg=new Holder<String>();
		String content="测试邮件内容"+Math.random()+"\r\n<a href=\"http://www.163.com\">163.com</a>";
//		soap.sendMailForEIP("BPMServer@aactechnologies.com", "saphrtest@aactechnologies.com", "测试邮件"+Math.random(), content, "s-BPM@aac.com", "AAC#1234abcd", "", "", sendResult, errmsg);
		soap.sendMailForEIP("BPMServer@aactechnologies.com", "GeXiao@aactechnologies.com", "测试邮件"+Math.random(), content, "s-BPM@aac.com", "AAC#1234abcd", "", "", sendResult, errmsg);
		*/
		/*SendMail sm = new SendMail();
		SendMailSoap soap = sm.getSendMailSoap();
		//System.out.println(soap.test());
		Holder<Boolean> sendResult = new Holder<Boolean>();
		Holder<String> errmsg = new Holder<String>();
		soap.sendMailForEIP("BPMServer@aactechnologies.com", "GeXiao@aactechnologies.com", "erwe",
				"wer", "s-BPM@aac.com", "AAC#1234abcd", "", "", sendResult, errmsg);
		System.out.println("邮件发送成功...");*/
		new EmailNotificationUtil().execute("s-BPM@aac.com", "AAC#1234abcd",  "GeXiao@aactechnologies.com", "erwe","we");
		//System.out.println("result:"+sendResult.value+", errmsg:"+errmsg.value);
	}

}
