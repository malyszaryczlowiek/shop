package com.malyszaryczlowiek.shop.converters;

import java.util.List;
import java.util.Map;

public interface Converter {
    List<String> convertFromStringToList(String stringToConvert);
    String convertFromListToString(List<String> listToConvert);

    Map<String, String> convertFromStringToMap(String stringToConvert);
    String convertFromMapToString(Map<String,String> mapToConvert);
}

