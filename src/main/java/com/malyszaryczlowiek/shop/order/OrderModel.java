package com.malyszaryczlowiek.shop.order;

import com.malyszaryczlowiek.shop.productOrder.ProductOrderModel;
import com.malyszaryczlowiek.shop.productOrder.ProductOrderModelAssembler;

import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderModel extends RepresentationModel<OrderModel> {

    // TODO to implement
    // trzeba zaimplementować List<ProductOrderModel> gdize
    // ProductOrderModel zawiera ProductModel itd

    private final List<ProductOrderModel> productOrderModels = new ArrayList<>();
    private final String status;
    private final Long orderDate;

    public OrderModel(Order order) {
        ProductOrderModelAssembler assembler = new ProductOrderModelAssembler();
        this.productOrderModels.addAll(
                order.getListOfProducts()
                        .stream()
                        .map(assembler::toModel)
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
