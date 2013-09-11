package org.caudexorigo.twitter.search;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

public class HttpHelper
{
	private static final long HTTP__CONNECT_TIMEOUT = 1500L;
	private static final long HTTP__READ_TIMEOUT = 3000L;
	private static final HttpClient http_client;

	static
	{
		HttpConnectionManager http_con_manager = new MultiThreadedHttpConnectionManager();
		http_con_manager.getParams().setDefaultMaxConnectionsPerHost(2);

		http_client = new HttpClient(http_con_manager);
		http_client.getParams().setConnectionManagerTimeout(HTTP__CONNECT_TIMEOUT);
		http_client.getParams().setSoTimeout((int) HTTP__READ_TIMEOUT);

	}

	public static HttpClient getHttpClient()
	{
		return http_client;
	}
}
