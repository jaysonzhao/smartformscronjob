package com.gzsolartech.bpmportal.util;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;



/**
 * @author Delon
 * 
 */

public class XMLClient {
	private static String _appContextName;

	private static final String XML_SERVICE_URI = "/XmlService";

	private HTTPClient httpClient = null;

	private String strResponse = null;

	private Document responseDoc = null;

	private String strRequest = null;
	
	public XMLClient() {
	}

	public static String getEncoding() {

		String encoding = "UTF-8";

		return encoding;
	}

	public String getContentType() {
		return "text/xml;charset=" + getEncoding();
	}

	private void setResponse(String s) {
		strResponse = s;
		responseDoc = null;
		if (s != null) {
			try {
				java.io.StringReader sr = new java.io.StringReader(s);
				org.xml.sax.InputSource is = new org.xml.sax.InputSource(sr);
				responseDoc = javax.xml.parsers.DocumentBuilderFactory
						.newInstance().newDocumentBuilder().parse(is);
			} catch (Exception e) {
				System.out
						.println("Exception caught parsing xml response, Error: "
								+ e.getLocalizedMessage());
			}
		}
	}

	public void open(Map<String,Object> config) throws Exception {
		String server=config.get("ashost").toString();
		String user=config.get("user").toString();
		String pwd=config.get("passwd").toString();
		open(user, pwd, server, false, "wfc", null);
	}

	public void open(String sUser, String sPassword, String sHostname,
			boolean fUseSSL, String appContext, String portNumber)
			throws Exception {

		// Get the logon XML request
		strRequest = getLoginRequest(sUser, sPassword);
		setResponse(null);

		// http or https
		StringBuffer url = new StringBuffer(fUseSSL ? "https://" : "http://");

		url.append(sHostname);
		if (portNumber != null && portNumber.length() > 0
				&& !portNumber.equals("80")) {
			url.append(":" + portNumber);
		}

		// add the App context
		url.append(getXMLServiceURI(appContext));
      //   System.out.println(url.toString());
		httpClient = new HTTPClient(url.toString(), fUseSSL);
		httpClient.setEncoding(getEncoding());
		httpClient.setContentType(getContentType());
		sendRequest(strRequest);
	}

	public void close() throws java.io.IOException {

		strRequest = getLogoffRequest();
		boolean isOpen = false;

		String response = null;
		try {
			httpClient.open();
			isOpen = true;
			httpClient.writeRequest(strRequest);
			response = httpClient.readResponse();
//System.out.println(response);
			httpClient.close();
			isOpen = false;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("XMLClient, Error" + e.getLocalizedMessage());
		} finally {
			if (isOpen) {
				httpClient.close();
			}
			httpClient = null;
		}

		if (response != null) {
			setResponse(response);
		}
	}

	public void sendRequest(String request) throws Exception {
		strRequest = request;
		boolean isOpen = false;
		String response = null;

		try {
			httpClient.open();
			isOpen = true;
			httpClient.writeRequest(request);
			//System.out.println(request);
			response = httpClient.readResponse();

			httpClient.close();
			isOpen = false;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("XMLClient, Error:" + e.getLocalizedMessage());
			throw e;
		} finally {
			if (isOpen) {
				httpClient.close();
			}
		}

		if (response != null) {
			setResponse(response);
		}
	}

	public String getXmlRequest() {
		return strRequest;
	}

	public String getXmlReply() {
		return strResponse;
	}

	public Document getDocument() {
		return responseDoc;
	}

	public static String getLoginRequest(String userName, String password) {
		String version = "1.0";
		String result = "<?xml version=\"1.0\" encoding=\"" + getEncoding()
				+ "\"?>\n" + "<KRONOS_WFC VERSION=\"" + version + "\">\n"
				+ getLoginRequestOnly(userName, password) + "</KRONOS_WFC>";
		return result;
	}

	public static String getLoginRequestOnly(String userName, String password) {
		String result = "<REQUEST OBJECT=\"SYSTEM\" ACTION=\"LOGON\" "
				+ "USERNAME=\"" + userName + "\" " + "PASSWORD=\"" + password
				+ "\"/>\n";
		return result;
	}

	public static String getLogoffRequest() {
		String version = "1.0";
		String result = "<?xml version='1.0' encoding=\"" + getEncoding()
				+ "\"?>" + "<KRONOS_WFC VERSION=\"" + version + "\">\n"
				+ getLogoffRequestOnly() + "</KRONOS_WFC>";
		return result;
	}

	public static String getLogoffRequestOnly() {
		return "<REQUEST OBJECT=\"SYSTEM\" ACTION=\"LOGOFF\"/>";
	}

	public static void setAppContextName(String acn) {
		_appContextName = acn;
	}

	public static String getAppContextName() {
		return _appContextName;
	}

	public static String getXMLServiceURI(String appContext) {
		String uri;

		if (appContext.indexOf("/") == 0) {
			uri = appContext + XML_SERVICE_URI;
		} else {
			uri = "/" + appContext + XML_SERVICE_URI;
		}
     //  System.out.println("uri="+uri);
		return uri;
	}

	public ArrayList getCookies() {
		ArrayList cookies = null;
		if (httpClient != null) {
			cookies = httpClient.getCookies();
		}
		return cookies;
	}

}
