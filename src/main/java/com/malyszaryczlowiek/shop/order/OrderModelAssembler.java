package com.malyszaryczlowiek.shop.order;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class OrderModelAssembler implements RepresentationModelAssembler<Order, OrderModel> {

    @Override
    public OrderModel toModel(Order entity) {
        return new OrderModel(entity);
    }

    /**
     * always must return null
     */
    @Override
    public CollectionModel<OrderModel> toCollectionModel(Iterable<? extends Order> entities) {
        return null;
    }
}
