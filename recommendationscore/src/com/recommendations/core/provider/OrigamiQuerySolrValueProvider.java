package com.recommendations.core.provider;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;



public class OrigamiQuerySolrValueProvider extends AbstractQuerySolrValueResolver {

    @Resource
    private FlexibleSearchService flexibleSearchService;


    @Override
    protected void addFieldValues(InputDocument inputDocument, IndexerBatchContext indexerBatchContext, IndexedProperty indexedProperty, ProductModel productModel, ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException {
        final String queryParam = getQuery(indexedProperty);

        if (StringUtils.isNotEmpty(queryParam)) {
            final FlexibleSearchQuery query = new FlexibleSearchQuery(queryParam, Map.of("product", productModel));
            query.setResultClassList(List.of(String.class));

            final SearchResult<String> result = flexibleSearchService.search(query);
            inputDocument.addField(indexedProperty, result.getResult());
        }
    }
}
