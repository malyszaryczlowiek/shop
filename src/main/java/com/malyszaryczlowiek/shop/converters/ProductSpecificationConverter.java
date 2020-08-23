package com.malyszaryczlowiek.shop.converters;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class ProductSpecificationConverter implements Converter {

    private final String listSplitter;
    private final String mapSplitter;

    public ProductSpecificationConverter(String listSplitter, String mapSplitter) {
        this.listSplitter = listSplitter;
        this.mapSplitter  = mapSplitter;
    }

    @Override
    public List<String> convertFromStringToList(String stringToConvert) {
        return null;
    }

    @Override
    public String convertFromListToString(List<String> listToConvert) {
        return null;
    }

    @Override
    public Map<String, String> convertFromStringToMap(String stringToConvert) {
        List<String> specifications = Arrays.asList(stringToConvert.split(listSplitter));
        Map<String, String> map = new LinkedHashMap<>(specifications.size());
        for (String s: specifications) {
            String[] spec = s.split(mapSplitter);
            map.putIfAbsent(spec[0], spec[1]);
        }
        return map;
    }

    @Override
    public String convertFromMapToString(Map<String, String> mapToConvert) {
        int mapSize = mapToConvert.size();
        if (mapSize > 0){
            StringBuilder builder = new StringBuilder();
            AtomicInteger i = new AtomicInteger(0);
            mapToConvert.forEach(
                    (k,v) -> {
                        builder.append(k).append(mapSplitter).append(v);
                        if (mapSize != 1 && i.getAndIncrement() < mapSize)
                            builder.append(listSplitter);
                    }
            );
            return builder.toString();
        }
        return ""; // todo tutaj jeszcze sprawdzić czy można zwrócić pusty string
    }
}
