/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.recommendations.core.controller;

import com.recommendations.core.data.SolrIndexedData;
import com.recommendations.core.dto.RelatedProductsDTO;
import com.recommendations.core.facade.OrigamiFacade;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {


    @Autowired
    private OrigamiFacade origamiFacade;
    /**
     * Endpoint to get related products for a given product code and strategy.
     *
     * @param productCodes the product code
     * @param strategy the strategy name
     * @return the related products as a DTO that supports JSON or XML
     * @throws FacetConfigServiceException if there is an error with the Solr configuration
     * @throws NoValidSolrConfigException if no valid Solr configuration is found
     * @throws FacetSearchException if there's a problem during the facet search
     */
    @GetMapping(value = "/related", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public RelatedProductsDTO getRelatedProducts(
            @RequestParam("productCodes") List<String> productCodes,
            @RequestParam("strategy") String strategy)
            throws FacetConfigServiceException, FacetSearchException, NoValidSolrConfigException {

        Optional<List<SolrIndexedData>> relatedProducts = origamiFacade.findRelatedProducts(productCodes, strategy);
        return new RelatedProductsDTO(relatedProducts.orElseGet(List::of));
    }
}
