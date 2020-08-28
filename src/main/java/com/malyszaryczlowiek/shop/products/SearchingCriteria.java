package com.malyszaryczlowiek.shop.products;

import java.util.List;
import java.util.Map;

public class SearchingCriteria {

    // kategoria nie jest potrzebna bo jest pobierana ze ścieżki
    private Map<String, List<String>> searchingParameters;

    public SearchingCriteria() {}

    public SearchingCriteria(Map<String, List<String>> searchingParameters) {
        this.searchingParameters = searchingParameters;
    }

    public Map<String, List<String>> getSearchingParameters() {
        return searchingParameters;
    }

    public void setSearchingParameters(Map<String, List<String>> searchingParameters) {
        this.searchingParameters = searchingParameters;
    }
}
