package org.caudexorigo.http.netty;

import java.io.FileInputStream;
import java.security.KeyStore;

import org.apache.commons.lang3.StringUtils;

public class HttpSslContext
{
	private final int sslPort;
	private final String keyPasswordStr;
	private final String keyStoreLocation;
	private final String keyStorePasswordStr;

	public HttpSslContext(int sslPort, String keyStoreLocation, String keyStorePasswordStr, String keyPasswordStr)
	{
		super();

		if (StringUtils.isBlank(keyStoreLocation))
		{
			throw new IllegalArgumentException("Invalid keystore location provided");
		}

		if (StringUtils.isBlank(keyStorePasswordStr))
		{
			throw new IllegalArgumentException("Invalid keystore password provided");
		}

		if (StringUtils.isBlank(keyPasswordStr))
		{
			throw new IllegalArgumentException("Invalid key password provided");
		}

		this.sslPort = sslPort;
		this.keyStoreLocation = keyStoreLocation;
		this.keyStorePasswordStr = keyStorePasswordStr;
		this.keyPasswordStr = keyPasswordStr;
	}

	public int getSslPort()
	{
		return this.sslPort;
	}

	public javax.net.ssl.SSLContext getSSLContext() throws Exception
	{
		KeyStore keyStore = KeyStore.getInstance("JKS");

		char[] KEYSTOREPW = keyStorePasswordStr.toCharArray();
		char[] KEYPW = keyPasswordStr.toCharArray();

		keyStore.load(new FileInputStream(keyStoreLocation), KEYSTOREPW);

		javax.net.ssl.KeyManagerFactory kmf = javax.net.ssl.KeyManagerFactory.getInstance("SunX509");

		kmf.init(keyStore, KEYPW);

		//javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("SSLv3");
		javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("TLS");

		sslContext.init(kmf.getKeyManagers(), null, null);

		return sslContext;
	}
}