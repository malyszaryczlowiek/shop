package com.malyszaryczlowiek.shop.order;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class OrderModel extends RepresentationModel<OrderModel> {



    public OrderModel(Order order) {


        this.add(addLinks());
    }

    private List<Link> addLinks() {
        return List.of();
    }
}
