package com.malyszaryczlowiek.shop.interSession;

import com.malyszaryczlowiek.shop.products.ProductIdOrder;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InterSessionShoppingCartContainer {

    private final ConcurrentHashMap<String, List<ProductIdOrder>> container = new ConcurrentHashMap<>(1000);


}
