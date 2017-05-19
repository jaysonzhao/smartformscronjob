package com.gzsolartech.bpmportal.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.SSLSocketFactory;

/**
 * @author Delon
 * 
 */
public class HTTPClient {
	protected String encoding = "ISO-8859-1";

	protected String contentType = "text/xml";

	protected String method = "POST";

	protected String urlString = null;

	protected URL url = null;

	protected ArrayList cookies = null;

	protected HttpURLConnection conn = null;

	protected BufferedWriter output = null;

	protected InputStream input = null;

	protected boolean ssl = false;

	private static SSLSocketFactory sslSocketFactory = null;

	protected boolean DEBUG_MODE = true;

	/**
	 * Create new client.
	 * 
	 * @param purl
	 *            to open a connection for
	 */
	public HTTPClient(String purl, boolean ssl) {
		urlString = purl;

		/*
		 * if (ssl) { this.ssl = ssl; initHTTPS(); }
		 */

	}

	protected HttpURLConnection getConnection() {
		return conn;
	}

	/*
	 * public void initHTTPS() { // PLF-9557 - SSL Changes
	 * System.setProperty("java.protocol.handler.pkgs", "javax.net.ssl"); // set
	 * up the trust manager X509TrustManager tm = new
	 * com.kronos.wfc.platform.sslsecurity.framework.KronosX509TrustManager();
	 * KeyManager[] km = null;
	 * 
	 * TrustManager[] tma = { tm };
	 * 
	 * try { SSLContext sslContext = SSLContext.getInstance("SSL");
	 * 
	 * sslContext.init(km, tma, new java.security.SecureRandom());
	 * 
	 * synchronized (HTTPClient.class) { // save this ssl socket factory if
	 * (sslSocketFactory == null) sslSocketFactory =
	 * sslContext.getSocketFactory(); } } catch (Exception e) {
	 * System.out.println("Failed to get our SSL Socket Factory"); }
	 * 
	 * if (DEBUG_MODE) {
	 * System.out.println("initHTTPS(): java.protocol.handler.pkgs=" +
	 * System.getProperty("java.protocol.handler.pkgs")); }
	 * 
	 * }
	 */

	public void setMethod(String newMethod) {
		method = newMethod;
	}

	public void setEncoding(String newEncoding) {
		encoding = newEncoding;
	}

	public void setContentType(String newContentType) {
		contentType = newContentType;
	}

	public boolean isOpen() {
		return (conn != null);
	}

	public void open() throws IOException {
		// Make sure we don't already have an open connection
		if (conn != null)
			throw new IllegalStateException(
					"HTTPClient: Called open() on an already-open connection");

		url = new URL(urlString);
		conn = (HttpURLConnection) url.openConnection();

		/*
		 * if (ssl) { HTTPSSetup.setHostNameVerifier(conn); }
		 */

		// Set up previously-stored cookies, if any
		for (int i = 0; cookies != null && i < cookies.size(); i++) {
			conn.setRequestProperty("Cookie", (String) cookies.get(i));
		}

		// Set up basic parameters
		conn.setAllowUserInteraction(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod(method);
		conn.setRequestProperty("Content-Type", contentType);

		// Establish the connection to the server
		try {
			conn.connect();
		} catch (IOException e) {
			conn = null;
			throw e;
		}

		if (method.equalsIgnoreCase("POST")) {
			OutputStream os = conn.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, encoding);
			output = new BufferedWriter(osw);
		}

	}

	public void writeRequest(String data) throws IOException {
		// Make sure we have a connection
		if (conn == null)
			throw new IllegalStateException(
					"HTTPClient: Called writeRequest() on a closed connection");

		// Make sure we have a writer
		if (method.equalsIgnoreCase("GET"))
			throw new IllegalStateException(
					"HTTPClient: Called writeRequest() for a GET connection");

		if (output == null)
			throw new IllegalStateException(
					"HTTPClient: Called writeRequest() after beginning to read response");

		// Write the data to the server

		try {
			output.write(data);
			output.newLine();
		} finally {
			closeOutput();
		}

	}

	public String readResponse() throws IOException {
		// Make sure we have a connection
		if (conn == null)
			throw new IllegalStateException(
					"HTTPClient: Called readResponse() on a closed connection");

		HttpURLConnection connection = conn;

		closeOutput();

		int code = connection.getResponseCode();
		if (code != HttpURLConnection.HTTP_OK)
			throw new IOException("HTTPClient: " + method + " " + url
					+ " failed: " + connection.getResponseMessage());

		int index = 1;
		while (true) {
			String name = connection.getHeaderFieldKey(index);
			if (name == null)
				break;
			String value = connection.getHeaderField(index);

			if (name.equalsIgnoreCase("Content-Type")) {
				contentType = value;

				int start = value.indexOf(";charset=");
				if (start != -1)
					encoding = value.substring(start + 9);
			} else if (name.equalsIgnoreCase("Set-Cookie")) {
				if (cookies == null)
					cookies = new ArrayList();

				int start = value.indexOf('=');
				String cookieName = value.substring(0, start);
				Iterator it = cookies.iterator();
				while (it.hasNext()) {
					String cookie = (String) it.next();
					if (cookie.startsWith(cookieName))
						it.remove();
				}

				cookies.add(value);
			}

			index++;
		}

		input = connection.getInputStream();
		InputStreamReader isr = new InputStreamReader(input, encoding);
		BufferedReader in = new BufferedReader(isr);

		StringBuffer buffer = new StringBuffer();
		int c;
		while ((c = in.read()) > -1) {
			buffer.append((char) c);
		}

		return buffer.toString();
	}

	public void close() {
		// Make sure we have a connection
		if (conn == null)
			throw new IllegalStateException(
					"HTTPClient: Called close() on an already-closed connection");

		closeOutput();
		closeInput();

		// Then close the connection itself
		if (conn != null) {
			conn.disconnect();
			conn = null;
		}
	}

	private void closeOutput() {
		if (output == null)
			return;

		try {
			output.flush();
			output.close();
		} catch (IOException ex) {
			if (DEBUG_MODE) {
				ex.printStackTrace();
				System.out.println("HTTPClient:closeOutput(), Error:"
						+ ex.getLocalizedMessage());
			}
		}

		output = null;
	}

	private void closeInput() {
		if (input == null)
			return;

		try {
			input.close();
		} catch (IOException ex) {
			if (DEBUG_MODE) {
				ex.printStackTrace();
				System.out.println("HTTPClient:closeOutput(), Error:"
						+ ex.getLocalizedMessage());
			}
		}

		input = null;
	}

	public ArrayList getCookies() {
		return cookies;
	}

}
