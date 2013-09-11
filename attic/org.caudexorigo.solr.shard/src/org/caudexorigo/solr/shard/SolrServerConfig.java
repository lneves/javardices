package org.caudexorigo.solr.shard;

import java.net.MalformedURLException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides handy bean for configuring an instance of org.apache.solr.client.solrj.SolrServer
 * 
 */
public class SolrServerConfig
{

	private static Logger logger = LoggerFactory.getLogger(SolrServerConfig.class.getName());

	private SolrServer solrServer;

	// default to localhost
	private String solrHost = "localhost";

	public void setSolrHost(String solrHost)
	{
		this.solrHost = solrHost;
	}

	// default to the default tomcat port
	private int solrPort = 8080;

	public void setSolrPort(int solrPort)
	{
		this.solrPort = solrPort;
	}

	public String toString()
	{
		return String.format("%s:%d/solr", solrHost, solrPort);
	}

	/**
	 * Initializes the SolrServer. Must be called before use. instance
	 */
	public void init()
	{

		try
		{

			solrServer = new CommonsHttpSolrServer(String.format("http://%s:%d/solr/", solrHost, solrPort));

		}
		catch (MalformedURLException malUrlException)
		{

			// Throw an exception back. Let the webapp startup, since the Solr
			// Server could
			// be bounced independantly of the webapp
			// The user must try to fix this
			logger.error("could not create the SolrServer bean on:" + solrServer + ":" + solrPort + "with error:" + malUrlException.getLocalizedMessage());
		}
		catch (Exception e)
		{

			// Any other exception from the Solr Server.
			// Log an error message, and let the web application come up.
			// The next successive call to getSolrServer() will try to
			// initialize again.
			logger.error("could not create the SolrServer bean on:" + solrServer + ":" + solrPort);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SolrServer getSolrServer()
	{
		return solrServer;
	}

}
