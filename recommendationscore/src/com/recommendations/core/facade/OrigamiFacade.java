package com.recommendations.core.facade;

import com.recommendations.core.data.SolrIndexedData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrigamiFacade {

    Optional<List<SolrIndexedData>> findRelatedProducts(List<String> product, String strategy) throws FacetConfigServiceException, NoValidSolrConfigException, FacetSearchException;

    Set<String> getAvailableStrategies();
}
