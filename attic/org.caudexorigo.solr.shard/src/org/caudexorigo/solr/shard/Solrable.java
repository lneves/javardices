package org.caudexorigo.solr.shard;


import org.apache.solr.common.SolrInputDocument;

/**
 * An interface for things that can be indexed into Solr.
 */
public interface Solrable {

    /**
     * Gets an object as a Solr input document suitable for indexing.
     */
    public SolrInputDocument toSolrDoc();
}
