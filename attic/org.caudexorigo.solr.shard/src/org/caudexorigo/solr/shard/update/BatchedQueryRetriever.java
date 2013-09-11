package org.caudexorigo.solr.shard.update;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.caudexorigo.solr.shard.SearchQuery;
import org.caudexorigo.solr.shard.SolrServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of BatchedQueryableDocumentRetriever that retrieves all Solr documents matching the documentSelectorQuery.
 * 
 * @author cstolte
 * 
 */
public class BatchedQueryRetriever implements BatchedQueryableDocumentRetriever
{

	protected static Logger logger = LoggerFactory.getLogger(BatchedQueryRetriever.class.getName());
	private SearchQuery query;
	private SolrServerConfig serverConfig;
	private SolrServer server;
	private SolrDocumentList results;
	private Set<String> keys;
	private String documentSelectorQuery;
	private long batchSize;
	private long start;
	private boolean queryIndexCreated;
	private String queryIndexPath;
	private BufferedReader fileReader;

	public BatchedQueryRetriever()
	{
		batchSize = 100; // defaults
		start = 0;
		documentSelectorQuery = "";
		queryIndexCreated = false;
	}

	public SolrDocumentList getNextBatch()
	{
		start += keys.size();

		// Use the keys to execute a search for full documents
		return queryDocuments();
	}

	public boolean hasMoreDocuments()
	{
		if (server == null)
		{
			throw new RuntimeException("Solr server is null - check configuration and solr server status.");
		}

		if (!queryIndexCreated)
		{
			createQueryIndex();
		}

		if (fileReader == null)
		{
			fileReader = openReader();
		}

		keys = getNextBatchFromIndex();
		return (keys.size() > 0);
	}

	private Set<String> getNextBatchFromIndex()
	{
		Set<String> nextBatch = new HashSet<String>();

		for (long i = start; i < start + batchSize; i++)
		{
			try
			{
				nextBatch.add(fileReader.readLine());
			}
			catch (IOException e)
			{
				logger.warn("I/O exception while reading record " + start);
			}
		}

		return nextBatch;
	}

	private BufferedReader openReader()
	{
		BufferedReader reader = null;

		try
		{
			reader = new BufferedReader(new FileReader(queryIndexPath));
		}
		catch (FileNotFoundException e)
		{
			logger.error("Unable to open query index file!  Aborting update...");
			System.exit(1);
		}

		return reader;
	}

	private SolrDocumentList queryDocuments()
	{
		query = new SearchQuery("key", keys);
		return executeQuery(query);
	}

	private void createQueryIndex()
	{
		String indexQuery = documentSelectorQuery + "&fl=key";
		long recordCount = 0;

		logger.info("Creating query index at " + this.queryIndexPath);

		query = new SearchQuery(indexQuery);
		query.addSortField("timestamp", ORDER.asc);
		query.setRows(-1L);
		query.setPagination(start);

		results = executeQuery(query);

		try
		{
			// Open a file for writing
			File outFile = new File(queryIndexPath);
			BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));

			for (SolrDocument result : results)
			{
				writer.write(result.getFieldValue("key").toString());
				recordCount++;
			}
		}
		catch (IOException e)
		{
			logger.warn("Error writing query index file: " + e.getStackTrace());
			System.exit(1);
		}

		queryIndexCreated = true;

		logger.info(String.format("Successfully created query index with %d records", recordCount));
	}

	private SolrDocumentList executeQuery(SearchQuery q)
	{
		SolrQuery solrQuery = q.getSolrQuery();

		long start = System.currentTimeMillis();

		logger.debug("Executing query " + q.toString());

		QueryResponse response = null;
		try
		{
			response = server.query(solrQuery, q.getSolrMethod());
		}
		catch (SolrServerException e)
		{
			logger.warn("Error executing solr query " + solrQuery.getQuery() + "Cause: " + e.getRootCause());
		}

		logger.info(String.format("Solr execution time reported %d, client actual %d", response.getElapsedTime(), (System.currentTimeMillis() - start)));

		return response.getResults();
	}

	public void setBatchSize(long size)
	{
		this.batchSize = size;
	}

	public void setStart(long start)
	{
		this.start = start;
	}

	public void setDocumentSelectorQuery(String query)
	{
		this.documentSelectorQuery = query;
	}

	public void setQueryIndexPath(String path)
	{
		this.queryIndexPath = path;
	}

	public void setServerConfig(SolrServerConfig config)
	{
		this.serverConfig = config;
		this.server = serverConfig.getSolrServer();
	}

}
