package com.recommendations.core.facade.impl;

import com.recommendations.core.data.SolrIndexedData;
import com.recommendations.core.facade.OrigamiFacade;
import com.recommendations.core.search.OrigamiSearchResult;
import de.hybris.platform.commercefacades.search.data.SearchFilterQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.FilterQueryOperator;
import de.hybris.platform.commerceservices.search.solrfacetsearch.impl.DefaultSolrFacetSearchProductSearchStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.daos.SolrFacetSearchConfigDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.FacetSearchStrategy;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.util.*;

import static com.recommendations.core.constants.RecommendationscoreConstants.ORIGAMI_SOLR_CONFIG_NAME;

public class OrigamiFacadeImpl extends DefaultSolrFacetSearchProductSearchStrategy<Object> implements OrigamiFacade {

    private static final Logger LOG = Logger.getLogger(OrigamiFacadeImpl.class);

    @Resource(name = "origamiSearchStrategy")
    private FacetSearchStrategy origamiSearchStrategy;

    @Resource
    private SolrFacetSearchConfigDao solrFacetSearchConfigDao;

    @Resource
    private ConfigurationService configurationService;


    @Override
    public Optional<List<SolrIndexedData>> findRelatedProducts(List<String> product, String strategy) throws FacetConfigServiceException, FacetSearchException {
        final OrigamiSearchResult origamiSearchResult = (OrigamiSearchResult) origamiSearchStrategy.search(createSearchQuery(product), Map.of());
        var strategyResults = origamiSearchResult.getOrigamiResult().get(strategy);
        if (strategyResults != null) {
            // Safe casting and filtering for empty lists
            if (!strategyResults.isEmpty()) {
                return Optional.of(strategyResults);
            }
            LOG.info("Invalid strategy result type for strategy: " + strategy);

        }

        // If the strategy is not found or not the expected type, return empty Optional
        return Optional.empty();
    }

    /**
     * Creates the search query with product code filter
     *
     * @param productCodes the product code to filter on
     * @return a configured SearchQuery
     */
    protected SearchQuery createSearchQuery(List<String> productCodes) throws FacetConfigServiceException {
        final FacetSearchConfig config = getFacetSearchConfig();
        final IndexedType type = getIndexedType(config);
        final SearchQuery searchQuery = new SearchQuery(config, type);
        searchQuery.addFilterQuery(ProductModel.CODE, SearchQuery.Operator.OR, productCodes.toArray(String[]::new));
        searchQuery.setPageSize(productCodes.size());
        return searchQuery;
    }


    /**
     * Retrieves the facet search configuration
     *
     * @return the FacetSearchConfig
     * @throws FacetConfigServiceException if there is a problem with the facet configuration service
     */
    protected FacetSearchConfig getFacetSearchConfig() throws FacetConfigServiceException {
        final SolrFacetSearchConfigModel config = solrFacetSearchConfigDao.findFacetSearchConfigByName(configurationService.getConfiguration().getString(ORIGAMI_SOLR_CONFIG_NAME));
        return getFacetSearchConfigService().getConfiguration(config.getName());
    }
}
