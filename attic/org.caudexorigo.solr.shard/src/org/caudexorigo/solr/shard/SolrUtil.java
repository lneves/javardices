package org.caudexorigo.solr.shard;

import java.util.Collection;
import java.util.Map;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

/**
 * Utilities for Solr stuff.
 */
public class SolrUtil {

    /**
     * Copy a SolrDocument to a new SolrInputDocument instance
     * 
     * @param doc
     * @return
     */
    public static SolrInputDocument copy(SolrDocument existingDoc) {

        SolrInputDocument inputDoc = new SolrInputDocument();

        Map<String, Collection<Object>> fields = existingDoc
                .getFieldValuesMap();

        for (String fieldName : fields.keySet()) {
            Collection<Object> values = fields.get(fieldName);
            if (values.size() > 1) {
                inputDoc.setField(fieldName, values);
            } else {
                inputDoc.setField(fieldName, values.iterator().next());
            }
        }

        return inputDoc;
    }


    public static String toString(SolrInputDocument doc) {
        StringBuilder sb = new StringBuilder();
        for (String f : doc.getFieldNames()) {
            @SuppressWarnings("rawtypes")
			Collection c = doc.getFieldValues(f);
            if (c != null) {
                sb.append(String.format("%s: %s\n", f, c));
            } else {
                sb.append(String.format("%s: %s\n", f, doc.getFieldValue(f)));
            }
        }
        return sb.toString();
    }

}
