/*package com.recommendations.core.strategy;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import de.hybris.platform.solrfacetsearch.search.impl.DefaultFacetSearchStrategy;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import de.hybris.platform.solrfacetsearch.search.impl.SearchResultConverterData;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.IOException;
import java.util.Map;
import org.apache.log4j.Logger;

public class OrigamiFacetSearchStrategy extends DefaultFacetSearchStrategy {


    private static final Logger LOG = Logger.getLogger(OrigamiFacetSearchStrategy.class);

    public SearchResult origamiSearch(SearchQuery searchQuery, String field) throws FacetSearchException {
        validateQuery(searchQuery);

        try (SolrClient solrClient = initializeSolrClient(searchQuery)) {
            FacetSearchContext facetSearchContext = prepareFacetSearchContext(searchQuery);

            SolrQuery solrQuery = buildSolrQuery(facetSearchContext, searchQuery);
            solrQuery.addField(field);
            QueryResponse queryResponse = executeSolrQuery(solrClient, solrQuery, searchQuery.getFacetSearchConfig());

            return processSearchResults(facetSearchContext, queryResponse);
        } catch (SolrServerException | IOException |SolrServiceException | RuntimeException e) {
            return handleSearchException(e);
        }
    }

    private void validateQuery(SearchQuery searchQuery) {
        if (searchQuery == null || searchQuery.getFacetSearchConfig() == null) {
            throw new IllegalArgumentException("Search query and facet search config must not be null.");
        }
    }


    private SolrClient initializeSolrClient(SearchQuery searchQuery) throws SolrServiceException {
        FacetSearchConfig facetSearchConfig = searchQuery.getFacetSearchConfig();
        IndexedType indexedType = searchQuery.getIndexedType();

        SolrSearchProvider solrSearchProvider = getSolrSearchProviderFactory().getSearchProvider(facetSearchConfig, indexedType);
        SolrIndexModel activeIndex = getSolrIndexService().getActiveIndex(facetSearchConfig.getName(), indexedType.getIdentifier());
        Index index = solrSearchProvider.resolveIndex(facetSearchConfig, indexedType, activeIndex.getQualifier());

        return solrSearchProvider.getClient(index);  // Reused initialization
    }

    private FacetSearchContext prepareFacetSearchContext(SearchQuery searchQuery) throws FacetSearchException {
        FacetSearchConfig facetSearchConfig = searchQuery.getFacetSearchConfig();
        IndexedType indexedType = searchQuery.getIndexedType();

        // Use factory and initialize context in one step
        FacetSearchContext facetSearchContext = getFacetSearchContextFactory().createContext(facetSearchConfig, indexedType, searchQuery);
        facetSearchContext.getSearchHints().putAll(Map.of());
        getFacetSearchContextFactory().initializeContext();
        checkContext(facetSearchContext);

        return facetSearchContext;
    }

    private SolrQuery buildSolrQuery(FacetSearchContext facetSearchContext, SearchQuery searchQuery) {
        // Convert the search query to Solr query with potential caching
        SearchQueryConverterData converterData = new SearchQueryConverterData();
        converterData.setFacetSearchContext(facetSearchContext);
        converterData.setSearchQuery(searchQuery);
        SolrQuery solrQuery = getFacetSearchQueryConverter().convert(converterData);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Solr Query: " + solrQuery);
        }

        return solrQuery;
    }

    private QueryResponse executeSolrQuery(SolrClient solrClient, SolrQuery solrQuery, FacetSearchConfig facetSearchConfig) throws SolrServerException, IOException {
        SolrRequest.METHOD method = resolveQueryMethod(facetSearchConfig);
        return solrClient.query(solrQuery, method);  // Reused logic to execute query
    }

    private SearchResult processSearchResults(FacetSearchContext facetSearchContext, QueryResponse queryResponse) throws FacetSearchException {
        SearchResultConverterData resultData = new SearchResultConverterData();
        resultData.setFacetSearchContext(facetSearchContext);
        resultData.setQueryResponse(queryResponse);
        SearchResult searchResult = getFacetSearchResultConverter().convert(resultData);

        getFacetSearchContextFactory().getContext().setSearchResult(searchResult);
        getFacetSearchContextFactory().destroyContext();

        return searchResult;
    }

    private SearchResult handleSearchException(Exception e) throws FacetSearchException {
        LOG.error("Search failed due to an exception", e);
        getFacetSearchContextFactory().destroyContext(e);
        throw new FacetSearchException("Search failed: " + e.getMessage(), e);
    }
}

 */

