package com.recommendations.widgets;

import java.util.Arrays;
import java.util.List;

import com.recommendations.core.data.SolrIndexedData;
import com.recommendations.core.facade.OrigamiFacade;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;


public class RecommendationBackofficeController extends DefaultWidgetController {
    private static final long serialVersionUID = 1L;

    @WireVariable
    private transient OrigamiFacade origamiFacade;

    @Wire
    private Textbox productInput;

    @Wire
    private Textbox strategyInput;

    @Wire
    private Button searchBtn;

    @Wire
    private Listbox resultsList;

    @Override
    public void initialize(Component comp) {
        super.initialize(comp);
        Selectors.wireComponents(comp, this, false);

    }

    @ViewEvent(componentID = "searchBtn", eventName = Events.ON_CLICK)
    public void doSearch() throws InterruptedException, FacetConfigServiceException, FacetSearchException, NoValidSolrConfigException {
        handleSearch();
    }

    private void handleSearch() throws FacetConfigServiceException, FacetSearchException, NoValidSolrConfigException {
        final String productCode = productInput.getValue();
        final String strategy = strategyInput.getValue();

        resultsList.getItems().clear();

        if (productCode.isEmpty() || strategy.isEmpty()) {
            Messagebox.show("Please enter both product code and strategy name.");
            return;
        }

        List<SolrIndexedData> results = origamiFacade.findRelatedProducts(Arrays.asList(productCode), strategy).get();

        for (SolrIndexedData data : results) {
            Listitem item = new Listitem();
            item.appendChild(new Listcell(data.getId()));
            item.appendChild(new Listcell(String.valueOf(data.getScore())));
            resultsList.appendChild(item);
        }
    }
}
