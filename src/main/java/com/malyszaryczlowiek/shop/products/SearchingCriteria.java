package com.malyszaryczlowiek.shop.products;

import java.util.List;
import java.util.Map;

public class SearchingCriteria {

    /**
     * kategoria nie jest potrzebna bo jest pobierana ze ścieżki.
     * Kluczem jest tutaj descryptor z
     * {@link com.malyszaryczlowiek.shop.feature.Feature#featureSearchingDescriptor
     * Feature#featureSearchingDescriptor}
     * Wartością jest natomiast lista parametrów-wartości po jakich należy szukać
     *
     */
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
