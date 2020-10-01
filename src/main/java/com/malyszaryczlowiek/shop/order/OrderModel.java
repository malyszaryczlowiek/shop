package com.malyszaryczlowiek.shop.order;

import com.malyszaryczlowiek.shop.productOrder.ProductOrderModel;

import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Klasa dostarcza model danych w postaci czy dane zamówienie jest
 * już zrealizowane, kiedy zostało zrealizowane oraz jakie produkty
 * zawierało.
 */
public class OrderModel extends RepresentationModel<OrderModel> {


    private final List<ProductOrderModel> productOrderModels = new ArrayList<>();
    private final String status;
    private final Long orderDate;

    public OrderModel(Order order) {
        this.productOrderModels.addAll(
                order.getListOfProducts()
                        .stream()
                        .map(ProductOrderModel::new)
                        .collect(Collectors.toList())
        );
        this.status = order.getStatus();
        this.orderDate = order.getOrderDate();
    }

    public List<ProductOrderModel> getProductOrderModels() {
        return productOrderModels;
    }

    public String getStatus() {
        return status;
    }

    public Long getOrderDate() {
        return orderDate;
    }
}
