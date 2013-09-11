package org.caudexorigo.solr.shard.update;

import org.apache.solr.common.SolrDocumentList;
import org.caudexorigo.solr.shard.SolrServerConfig;

/**
 * A BatchedQueryableDocumentRetriever queries a Solr server (query is set by client)
 * and returns the results in batches.  Results are returned in ascending order of
 * document time stamp.
 * 
 * @author cstolte
 *
 */
public interface BatchedQueryableDocumentRetriever {
	
	/**
	 * Get next batch of documents.
	 * @return
	 */
	SolrDocumentList getNextBatch();
	
	/**
	 * Return true if there are more documents remaining.
	 * @return
	 */
	boolean hasMoreDocuments();
	
	/**
	 * Set batch size (number of documents to process at a time.)
	 * @param size
	 */
	void setBatchSize(long size);
	
	/**
	 * First record to return - a value of 420 would cause getNextBatch()
	 * to return records 420 through (420 + batchSize) on the first
	 * call.
	 * @param start
	 */
	void setStart(long start);
	
	/**
	 * The Solr query string that will determine which documents
	 * are selected and returned.  This corresponds to the value
	 * of the q parameter in a Solr query string.
	 * @param query
	 */
	void setDocumentSelectorQuery(String query);
	
	/**
	 * A file to locally store the list of keys used to request
	 * batches of documents from the server.
	 * @param path
	 */
	void setQueryIndexPath(String path);
	
	/**
	 * A Solr server to query and select documents from.
	 * @param config
	 */
	void setServerConfig(SolrServerConfig config);
}
