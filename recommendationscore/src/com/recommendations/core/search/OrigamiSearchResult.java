package com.recommendations.core.search;

import com.recommendations.core.data.SolrIndexedData;
import de.hybris.platform.solrfacetsearch.search.impl.SolrSearchResult;


import java.util.List;
import java.util.Map;

public class OrigamiSearchResult extends SolrSearchResult {

    private Map<String, List<SolrIndexedData>> origamiResult;

    public Map<String, List<SolrIndexedData>> getOrigamiResult() {
        return origamiResult;
    }

    public void setOrigamiResult(Map<String, List<SolrIndexedData>> origamiResult) {
        this.origamiResult = origamiResult;
    }
}
