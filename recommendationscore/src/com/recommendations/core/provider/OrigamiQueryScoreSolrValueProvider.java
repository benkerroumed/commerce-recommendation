package com.recommendations.core.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recommendations.core.data.SolrIndexedData;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrigamiQueryScoreSolrValueProvider extends AbstractQuerySolrValueResolver {

    @Resource
    private FlexibleSearchService flexibleSearchService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void addFieldValues(InputDocument inputDocument,
                                  IndexerBatchContext indexerBatchContext,
                                  IndexedProperty indexedProperty,
                                  ProductModel productModel,
                                  ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException {

        final String queryParam = getQuery(indexedProperty);

        if (StringUtils.isNotEmpty(queryParam)) {
            // Create the FlexibleSearch query and bind the product model
            FlexibleSearchQuery query = new FlexibleSearchQuery(queryParam, Map.of("product", productModel));
            query.setResultClassList(List.of(String.class, Long.class));

            // Execute the query and get the result
            SearchResult<List<Object>> result = flexibleSearchService.search(query);

            // Check if the result is empty
            if (result.getResult().isEmpty()) {
                return;  // No result, so return early
            }

            // Process the results
            final List<SolrIndexedData> solrIndexedDataList = processQueryResults(result.getResult());

            // Transform list of SolrIndexedData to JSON
            final String json = convertToJson(solrIndexedDataList);

            // Add the field to the input document
            inputDocument.addField(indexedProperty, json);
        }
    }

    /**
     * Process the search query results into a list of SolrIndexedData.
     *
     * @param resultList List of results from FlexibleSearch query
     * @return List of SolrIndexedData objects
     */
    private List<SolrIndexedData> processQueryResults(List<List<Object>> resultList) {
        List<SolrIndexedData> solrIndexedDataList = new ArrayList<>();

        for (List<Object> result : resultList) {
            if (result.size() == 2) {
                String id = (String) result.get(0);
                final Long score = (Long) result.get(1);

                SolrIndexedData solrIndexedData = new SolrIndexedData();
                solrIndexedData.setId(id);
                solrIndexedData.setScore(score);
                solrIndexedDataList.add(solrIndexedData);
            }
        }

        return solrIndexedDataList;
    }

    /**
     * Converts a list of SolrIndexedData to a JSON string.
     *
     * @param solrIndexedDataList List of SolrIndexedData
     * @return JSON representation as a String
     */
    private String convertToJson(List<SolrIndexedData> solrIndexedDataList) {
        try {
            // Convert the list of SolrIndexedData objects to JSON using Jackson's ObjectMapper
            return objectMapper.writeValueAsString(solrIndexedDataList);
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }
}
