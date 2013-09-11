package org.caudexorigo.solr.shard;

import java.util.List;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Boathouse Implementation of a Solr Query Analyzer/Parser.
 * 
 */
public class SearchQuery {
    
	private static Logger logger = LoggerFactory.getLogger(SearchQuery.class.getName());
    private SolrRequest.METHOD method = SolrRequest.METHOD.GET;
    private SolrQuery solrQuery;

    /**
     * Default constructor which creates a query like this q=....
     * 
     * @param queryString
     */
    public SearchQuery(String queryString) {
        String escapedQueryString = queryString; 
        this.solrQuery = new SolrQuery(escapedQueryString);
        
        // If the query includes an explicit Boolean operator, don't use the dismax query type
        // because it causes unexpected results.
        if (queryString.matches(".*(AND|OR).*")) {
            solrQuery.setQueryType("standard");
        }
        
        // Detault to returning all facet results
        this.solrQuery.setFacetLimit(-1);
        
        verifyRequestMethod(escapedQueryString);
    }
    
    /**
     * Constructor which creates a Solr Query like
     * q=fieldName:fieldValue+fieldName:fieldValue...
     * 
     * @param fieldName
     * @param fieldValues
     */
    public SearchQuery(String fieldName, Set<String> fieldValues) {

        if (fieldName == null || fieldName.isEmpty() || fieldValues == null
                || fieldValues.isEmpty()) {
            return;
        }

        StringBuilder qs = new StringBuilder();
        for (String id : fieldValues) {
            qs.append(fieldName).append(":").append( ClientUtils.escapeQueryChars(id) )
                    .append(" ");
        }
        
        String escapedQueryString = qs.toString();
        verifyRequestMethod(escapedQueryString);
        
        this.solrQuery = new SolrQuery(escapedQueryString);
    }

    /**
     * Max number of facet results
     * @see http://wiki.apache.org/solr/SimpleFacetParameters#facet.limit
     * @param limit
     */
    public void setFacetLimit(int limit) {
        this.solrQuery.setFacetLimit(limit);
    }

    /**
     * Filter the Query by the given field name Basically this creates a Solr
     * "fq" FilterQuery request. 
     * 
     * fq=fieldName:fieldValue+fieldName:fieldValue...
     * 
     * @param fieldName
     * @param fieldValues
     */
    public void filterByField(String fieldName, Set<String> fieldValues) {

        if (fieldName == null || fieldName.isEmpty() || fieldValues == null
                || fieldValues.isEmpty()) {
            return;
        }

        StringBuilder qs = new StringBuilder();
        for (String id : fieldValues) {
            qs.append(fieldName).append(":").append( ClientUtils.escapeQueryChars(id) )
                    .append(" ");
        }

        this.solrQuery.addFilterQuery(qs.toString());

        // If the Length of the fq is > 1024, set the method as POST for the
        // query to execute
        verifyRequestMethod(qs.toString());
    }
    
    public void setFields(String fields) {
    	this.solrQuery.setFields(fields);
    }
    
    public void addExplicitQuery(String query) {
        String currentQuery = this.solrQuery.getQuery();
        if (currentQuery.length() > 0) {
          currentQuery+= String.format(" AND (%s)", query);
        }
        else {
          currentQuery= query;
        }
        
        this.solrQuery.setQuery(currentQuery);
    }
    
    /**
     * Order by a particular fields
     * 
     * @param field
     * @param orderBy
     */
    public void addSortField(String field, SolrQuery.ORDER order) {
        this.solrQuery.addSortField(field, order);
    }

    /**
     * Set the number of rows to return back in the result set. Default value
     * set is 100
     * 
     * @param rows
     */
    public void setRows(Long rows) {
        if (rows != null) {
            this.solrQuery.setRows(rows.intValue());
        }
    }

    /**
     * Set the pagination parameters.
     * 
     * @param startFrom
     *            - sets the start of resultSet. The total elements returned
     *            back is set by the "rows" for this Query
     * @see setRows
     */
    public void setPagination(Long startFrom) {
        if (startFrom != null) {
            this.solrQuery.setStart(startFrom.intValue());
        }
    }

    /**
     * Turn ON/OFF the facet. By default its OFF. If ON - by default - All the
     * facets whose count is > 1, will be returned
     * 
     * facet=true&facet.mincount=1&facet.field=facetfield1&facet.field=
     * facetField2...
     * 
     * @param facet
     * @param facetFields
     */
    public void turnFacetOn(boolean facet, List<String> facetFields) {

        if (!facet) {
            return;
        }

        this.solrQuery.setFacet(facet);
        this.solrQuery.setFacetMinCount(1);
        for (String facetField : facetFields) {
            this.solrQuery.addFacetField(facetField);
        }
    }

    /**
     * Turn ON the highlighting feature
     * 
     * @param highlight
     */
    public void turnHighlightsOn(boolean highlight) {
        this.solrQuery.setHighlight(highlight);
    }

    /**
     * returns the {@link SolrQuery} implementation
     * 
     * @return
     */
    public SolrQuery getSolrQuery() {
        return this.solrQuery;
    }

    /**
     * The METHOD to be used to execute the Solr Query. If the length of "fq" is
     * > 1024, this returns a POST method, else GET
     * 
     * @return
     */
    public SolrRequest.METHOD getSolrMethod() {
        return this.method;
    }

    /**
     * GET requests can be no longer than 1024 characters.
     * @param queryString
     */
    public void verifyRequestMethod() {
        this.verifyRequestMethod(this.solrQuery.toString());
    }

    private void verifyRequestMethod(String queryString) {
        if (queryString.length() > 1024) {
            logger.debug("Query is large, switching to a POST method");
            this.method = SolrRequest.METHOD.POST;
        }
    }

    public String toString() {
        if (this.solrQuery != null) {
            return solrQuery.toString();
        }
        return "solr_query_not_set";
    }

}
