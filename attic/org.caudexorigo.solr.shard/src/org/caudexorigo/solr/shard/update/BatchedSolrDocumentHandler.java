package org.caudexorigo.solr.shard.update;

import java.util.List;

import org.apache.solr.common.SolrInputDocument;
import org.caudexorigo.solr.shard.SolrServerConfig;

/**
 * A BatchedSolrDocumentUpdater updates either single or batched documents. It's left up to the implementation class to decide how to update each document.
 * 
 * @author cstolte
 * 
 */
public interface BatchedSolrDocumentHandler
{

	/**
	 * Update a batch of documents, and optionally send them off to the server (otherwise the updated list will simply be returned to the client...)
	 * 
	 * @param documents
	 */
	List<SolrInputDocument> handleBatch(List<SolrInputDocument> documents, boolean process);

	/**
	 * Update a single document
	 * 
	 * @param document
	 * @param process
	 */
	SolrInputDocument handleDocument(SolrInputDocument document, boolean process);

	/**
	 * A Solr server to send document updates to.
	 * 
	 * @param config
	 */
	void setServerConfig(SolrServerConfig config);
}
