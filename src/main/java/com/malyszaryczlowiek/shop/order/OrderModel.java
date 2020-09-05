package com.malyszaryczlowiek.shop.order;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class OrderModel extends RepresentationModel<OrderModel> {

    // TODO to implement
    // trzeba zaimplementować List<ProductOrderModel> gdize
    // ProductOrderModel zawiera ProductModel itd

    to implement

    public OrderModel(Order order) {


        this.add(addLinks());
    }

    private List<Link> addLinks() {
        return List.of();
    }
}
