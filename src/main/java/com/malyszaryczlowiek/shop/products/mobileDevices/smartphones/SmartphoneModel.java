package com.malyszaryczlowiek.shop.products.mobileDevices.smartphones;

import com.malyszaryczlowiek.shop.products.ProductModel;


public class SmartphoneModel extends ProductModel {

    private final String screenRefreshing;

    public SmartphoneModel(Smartphone smartphone) {
        // na etapie wklejania tutaj smartfona, gdzie pobierany jest produkt
        // tracona jest cała informacja o atrybutach (sprcyfikacji smartfona - klasy dziedziczącje)
        super(smartphone);
        this.screenRefreshing = smartphone.getScreenRefreshing();
    }

    public String getScreenRefreshing() {
        return screenRefreshing;
    }
}



/*
Konstruktor w oparciu o mechanizm refleksji

public SmartphoneModel(Smartphone smartphone) {
        super();
        Map<String, String> map = new LinkedHashMap<>();
        Field[] fields = smartphone.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            String value = (String) field.get(tutaj jakoś trzeba wkleić pole z entity);
            map.put(fieldName, value);
        }
        super.setMapOfSpecification(map);
    }
 */