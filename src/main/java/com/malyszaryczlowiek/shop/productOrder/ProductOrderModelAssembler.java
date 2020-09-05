package com.malyszaryczlowiek.shop.productOrder;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class ProductOrderModelAssembler implements RepresentationModelAssembler<ProductOrder, ProductOrderModel> {

    @Override
    public ProductOrderModel toModel(ProductOrder entity) {
        return new ProductOrderModel(entity);
    }

    @Override
    public CollectionModel<ProductOrderModel> toCollectionModel(Iterable<? extends ProductOrder> entities) {
        return null;
    }
}
