package com.recommendations.widgets;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.hybris.backoffice.widgets.advancedsearch.AdvancedSearchMode;
import com.hybris.backoffice.widgets.advancedsearch.engine.AdvancedSearchEngineController;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.FieldType;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import com.recommendations.core.data.SolrIndexedData;
import com.recommendations.core.facade.OrigamiFacade;
import com.recommendations.core.service.OrigamiService;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;


public class RecommendationBackofficeController extends AdvancedSearchEngineController {
    private static final long serialVersionUID = 1L;

    @WireVariable
    private transient OrigamiFacade origamiFacade;

    @Wire
    private Textbox productInput;

    @Wire
    private Combobox strategyInput;

    @Wire
    private Button searchBtn;


    @Override
    public void initialize(Component comp) {
        super.initialize(comp);
        Selectors.wireComponents(comp, this, false);
        strategyInput.setModel(new ListModelList<>(origamiFacade.getAvailableStrategies()));

    }

    @ViewEvent(componentID = "searchBtn", eventName = Events.ON_CLICK)
    public void doSearch() throws InterruptedException, FacetConfigServiceException, FacetSearchException, NoValidSolrConfigException {
        handleSearch();
    }

    private void handleSearch() throws FacetConfigServiceException, FacetSearchException, NoValidSolrConfigException {
        final String productCode = productInput.getValue();
        final Comboitem selectedStrategy = strategyInput.getSelectedItem();

        if (productCode.isEmpty() || selectedStrategy == null) {
            Messagebox.show("Please enter a product code and select a strategy.");
            return;
        }

        final String strategy = selectedStrategy.getLabel();
        final List<SolrIndexedData> results = origamiFacade.findRelatedProducts(Arrays.asList(productCode), strategy).get();


        AdvancedSearchData searchData = new AdvancedSearchData();
        searchData.setAdvancedSearchMode(AdvancedSearchMode.ADVANCED);
        searchData.setTypeCode("Product");
        searchData.setGlobalOperator(ValueComparisonOperator.OR);

        for (SolrIndexedData data : results) {

            FieldType statusFieldType = new FieldType();
            statusFieldType.setDisabled(Boolean.FALSE);
            statusFieldType.setSelected(Boolean.TRUE);
            statusFieldType.setName(ProductModel.PK);
            searchData.addCondition(statusFieldType, ValueComparisonOperator.EQUALS, data.getId());
        }

        onSearchDataInput(searchData);


    }


}
