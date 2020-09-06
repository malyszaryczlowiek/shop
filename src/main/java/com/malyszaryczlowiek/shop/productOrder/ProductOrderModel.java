package com.malyszaryczlowiek.shop.productOrder;

import com.malyszaryczlowiek.shop.products.ProductModel;
import com.malyszaryczlowiek.shop.products.ProductModelAssembler;

import org.springframework.hateoas.RepresentationModel;

public class ProductOrderModel extends RepresentationModel<ProductOrderModel> {

    private final ProductModel productModel;
    private final Integer numberOfProducts;

    public ProductOrderModel(ProductOrder productOrder) {
        ProductModelAssembler assembler = new ProductModelAssembler();
        this.productModel = assembler.toModel(productOrder.getProduct());
        this.numberOfProducts = productOrder.getNumberOfOrderedProducts();

        // tutaj nie trzeba dodawać żadnych linków bo
        // bo linki mają zawierać tylko produkty
    }

    public ProductModel getProductModel() {
        return productModel;
    }

    public Integer getNumberOfProducts() {
        return numberOfProducts;
    }
}
