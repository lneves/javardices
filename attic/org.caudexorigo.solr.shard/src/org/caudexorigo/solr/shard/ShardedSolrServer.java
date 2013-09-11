package org.caudexorigo.solr.shard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShardedSolrServer
{

	private int nextQueryableServerIndex = 0;

	private AtomicLong lastCommitTime = new AtomicLong();

	public static String facetDelimiter = "\t";

	static Logger logger = LoggerFactory.getLogger(ShardedSolrServer.class.getName());

	private List<SolrServerConfig> servers = new ArrayList<SolrServerConfig>();

	public ShardedSolrServer(List<SolrServerConfig> servers)
	{
		this.servers = servers;
		logger.debug(String.format("Created SharderSolrServer with %d servers", servers.size()));
	}

	public QueryResponse query(SolrQuery solrQuery, SolrRequest.METHOD method) throws SolrServerException
	{

		if (servers.size() > 1)
		{
			StringBuilder shardUrls = new StringBuilder();
			for (int i = 0; i < servers.size(); i++)
			{
				shardUrls.append(servers.get(i).toString());

				// Not last?
				if (i < (servers.size() - 1))
				{
					shardUrls.append(",");
				}
			}
			solrQuery.setParam("shards", shardUrls.toString());
		}

		logger.debug(String.format("Executing sharded query %s", solrQuery.toString()));
		SolrServer solrServer = this.getQueryableSolrServer();
		QueryResponse response = solrServer.query(solrQuery, method);
		logger.debug(String.format("Got response from sharded query %s", solrQuery.toString()));
		return response;
	}

	public QueryResponse query(SearchQuery searchQuery) throws SolrServerException
	{
		SolrQuery solrQuery = searchQuery.getSolrQuery();
		return query(solrQuery, searchQuery.getSolrMethod());
	}

	/**
	 * Gets one of the sharded solr servers using a simple round-robin load balancing scheme
	 * 
	 * @return
	 */
	private synchronized SolrServer getQueryableSolrServer()
	{
		SolrServerConfig solr = servers.get(nextQueryableServerIndex);
		SolrServer server = solr.getSolrServer();
		logger.debug("Located queryable solr server %s", solr.toString());
		nextQueryableServerIndex++;
		if (nextQueryableServerIndex == servers.size())
		{
			nextQueryableServerIndex = 0;
		}
		return server;
	}

	/**
	 * Adds a batch of solr documents to solr TODO: Maybe add per-server batching later. If so, use a public commit() method call to signal the end of an operation. For now, just add one-by-one
	 * 
	 * @param docs
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public void add(Collection<SolrInputDocument> docs) throws SolrServerException, IOException
	{
		logger.debug(String.format("Adding %d docs to sharded solr servers", docs.size()));
		for (SolrInputDocument doc : docs)
		{
			this.add(doc);
		}
	}

	/**
	 * Adds a solr document to one of the sharded solr index servers, as determined by which server the document 'key' field hashes to
	 * 
	 * @param doc
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public UpdateResponse add(SolrInputDocument doc) throws SolrServerException, IOException
	{
		SolrInputField keyField = doc.getField("key");
		String key = (String) keyField.getValue();
		int keyHashCode = resourcePool.computeKeyHash(key);
		int serverIndex = resourcePool.locateResourceByKeyHash(keyHashCode);

		// Add the keyHashCode to the solr document before saving, in case we
		// ever need to migrate docs for a keyHash range from one server to
		// another
		doc.setField("keyHashCode", keyHashCode);

		SolrServerConfig solr = this.servers.get(serverIndex);
		SolrServer server = solr.getSolrServer();
		UpdateResponse response = server.add(doc);
		logger.debug(String.format("Added doc with key %s to server %s with response %s", key, solr.toString(), response.toString()));
		return response;
	}

	/**
	 * Load one solr document by unique key
	 * 
	 * @param searchQuery
	 * @return
	 * @throws SolrServerException
	 */
	public SolrDocument loadByKey(String key) throws SolrServerException
	{
		SearchQuery searchQuery = new SearchQuery("key:\"" + key + "\"");
		int keyHashCode = resourcePool.computeKeyHash(key);
		int serverIndex = resourcePool.locateResourceByKeyHash(keyHashCode);
		SolrServerConfig solr = this.servers.get(serverIndex);

		logger.debug(String.format("Looking up doc with key %s from server %d", key, serverIndex));
		QueryResponse response = solr.getSolrServer().query(searchQuery.getSolrQuery(), searchQuery.getSolrMethod());
		SolrDocumentList documentList = response.getResults();
		SolrDocument document = null;
		if (documentList.size() > 0)
		{
			document = documentList.get(0);
			logger.debug(String.format("Response from lookup for key %s : %s", key, document.toString()));
		}
		else
		{
			logger.debug(String.format("No document found for key %s", key));
		}
		return document;
	}

	private final long MIN_DELAY_BETWEEN_MANUAL_COMMITS = 5000;

	/**
	 * Manually commit docs added so far to all solr servers
	 */
	public void commitToAllServers()
	{

		// Enough time elapsed since last commit?
		if ((System.currentTimeMillis() - lastCommitTime.longValue()) < MIN_DELAY_BETWEEN_MANUAL_COMMITS)
		{
			logger.info(String.format("Not enough time since last commit, skipping commit to all %d solr servers", servers.size()));
			return;
		}
		lastCommitTime.set(System.currentTimeMillis());

		logger.debug(String.format("Committing to all %d solr servers", servers.size()));

		// Start commits at the same time
		Thread[] committerThreads = new Thread[servers.size()];
		for (int i = 0; i < servers.size(); i++)
		{
			Committer committer = new Committer(servers.get(i));
			committerThreads[i] = new Thread(committer);
			committerThreads[i].setName(String.format("Committer-%d", i));
			committerThreads[i].setDaemon(true);
			committerThreads[i].start();
		}

		// Wait for last commit to finish
		for (int i = 0; i < servers.size(); i++)
		{
			try
			{
				committerThreads[i].join();
			}
			catch (InterruptedException ie)
			{
				logger.warn("Interrupted while waiting for committer thread to die", ie);
			}

		}
		logger.debug(String.format("Committed to all %d solr servers", servers.size()));

	}

	private class Committer implements Runnable
	{

		private SolrServerConfig solrConfig;

		Committer(SolrServerConfig solrConfig)
		{
			this.solrConfig = solrConfig;
		}

		public void run()
		{
			logger.info(String.format("Committing to %s", solrConfig));
			try
			{

				// Commit. Note that we're using waitFlush and
				// waitSearcher so we'll wait until slowest server can be searched
				solrConfig.getSolrServer().commit(true, true);
				logger.info(String.format("Committed to %s", solrConfig));
			}
			catch (SolrServerException e)
			{
				logger.error(String.format("Exception committing to solr server %s", solrConfig));
			}
			catch (IOException e)
			{
				logger.error(String.format("Exception committing to server %s", solrConfig));
			}
		}
	}
}
