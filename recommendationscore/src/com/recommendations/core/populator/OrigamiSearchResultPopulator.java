package com.recommendations.core.populator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recommendations.core.data.SolrIndexedData;
import com.recommendations.core.search.OrigamiSearchResult;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.search.FieldNameTranslator;
import de.hybris.platform.solrfacetsearch.search.impl.SearchResultConverterData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

public class OrigamiSearchResultPopulator implements Populator<SearchResultConverterData, OrigamiSearchResult> {

    private static final Logger LOG = LoggerFactory.getLogger(OrigamiSearchResultPopulator.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Resource
    private FieldNameTranslator fieldNameTranslator;

    @Override
    public void populate(SearchResultConverterData source, OrigamiSearchResult target) {
        final List<SolrDocument> solrDocuments = source.getQueryResponse().getResults();

        if (CollectionUtils.isEmpty(solrDocuments)) {
            target.setOrigamiResult(Collections.emptyMap());
            return;
        }

        // propertyKey -> (id -> SolrIndexedData)
        final Map<String, Map<String, SolrIndexedData>> mergedResultsByProperty = new HashMap<>();
        solrDocuments.forEach(document -> accumulateDocumentResults(document, source, mergedResultsByProperty));

        // Convert inner maps to Lists and set to result
        final Map<String, List<SolrIndexedData>> mergedResultListByProperty = mergedResultsByProperty.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new ArrayList<>(entry.getValue().values())
                ));

        target.setOrigamiResult(mergedResultListByProperty);
    }

    private void accumulateDocumentResults(SolrDocument document,
                                           SearchResultConverterData searchContext,
                                           Map<String, Map<String, SolrIndexedData>> mergedResultsByProperty) {

        searchContext.getFacetSearchContext().getIndexedType().getIndexedProperties().keySet()
                .forEach(propertyKey -> {
                    final String solrFieldName = fieldNameTranslator.translate(searchContext.getFacetSearchContext(), propertyKey);
                    final Object rawJson = document.getFieldValue(solrFieldName);

                    final List<SolrIndexedData> indexedDataList = parseJsonFieldSafely(rawJson, propertyKey, solrFieldName);
                    if (indexedDataList.isEmpty()) return;

                    final Map<String, SolrIndexedData> propertyDataMap =
                            mergedResultsByProperty.computeIfAbsent(propertyKey, k -> new HashMap<>());

                    for (SolrIndexedData item : indexedDataList) {
                        propertyDataMap.merge(
                                item.getId(),
                                item,
                                (existing, incoming) -> {
                                    existing.setScore(existing.getScore() + incoming.getScore());
                                    return existing;
                                });
                    }
                });
    }

    private List<SolrIndexedData> parseJsonFieldSafely(Object rawValue, String propertyKey, String solrFieldName) {
        if (!(rawValue instanceof String json) || json.isBlank()) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            LOG.warn("Failed to parse JSON for Solr field '{}' (property '{}'): {}", solrFieldName, propertyKey, e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
