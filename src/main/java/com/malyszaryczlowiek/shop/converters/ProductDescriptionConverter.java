package com.malyszaryczlowiek.shop.converters;

import java.util.*;

/**
 * Converter który merguje listę stringów do jednego gdzie
 * każdy ze stringów z listy jest od następnego oddzielony
 * separatorem.
 *
 * TODO napisać test sprawdzający poprawność tych metod.
 */
public class ProductDescriptionConverter implements Converter  {

    private final String splitter;

    public ProductDescriptionConverter(String splitter) {
        this.splitter = splitter;
    }

    @Override
    public List<String> convertFromStringToList(String stringToConvert) {
        if (stringToConvert.contains(splitter))
            return Arrays.asList(stringToConvert.split(splitter));
        return Collections.singletonList(stringToConvert);
    }

    @Override
    public String convertFromListToString(List<String> listToConvert) {
        if (listToConvert.size() >1) {
            Iterator<String> iterator = listToConvert.listIterator();
            StringBuilder builder = new StringBuilder();
            while (iterator.hasNext()) {
                builder.append(iterator.next());
                if (iterator.hasNext())
                    builder.append(splitter);
            }
            return builder.toString();
        }
        else
            return listToConvert.get(0);
    }

    @Override
    public Map<String, String> convertFromStringToMap(String stringToConvert) {
        return null;
    }

    @Override
    public String convertFromMapToString(Map<String, String> mapToConvert) {
        return null;
    }
}
