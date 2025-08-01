package com.recommendations.core.dto;

import com.recommendations.core.data.SolrIndexedData;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "relatedProducts")
public class RelatedProductsDTO {

    private List<SolrIndexedData> products;

    public RelatedProductsDTO() {}

    public RelatedProductsDTO(List<SolrIndexedData> products) {
        this.products = products;
    }

    public List<SolrIndexedData> getProducts() {
        return products;
    }

    public void setProducts(List<SolrIndexedData> products) {
        this.products = products;
    }
}
