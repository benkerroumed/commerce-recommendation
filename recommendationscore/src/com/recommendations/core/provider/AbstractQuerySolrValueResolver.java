package com.recommendations.core.provider;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.solrfacetsearch.provider.impl.ValueProviderParameterUtils;
import org.apache.commons.lang3.StringUtils;

import static com.recommendations.core.constants.RecommendationscoreConstants.QUERY_PROVIDER_PARAM;

public abstract class AbstractQuerySolrValueResolver extends AbstractValueResolver<ProductModel, Object, Object> {

    protected String getQuery(IndexedProperty indexedProperty) {
        return ValueProviderParameterUtils.getString(indexedProperty, QUERY_PROVIDER_PARAM, StringUtils.EMPTY);
    }
}
