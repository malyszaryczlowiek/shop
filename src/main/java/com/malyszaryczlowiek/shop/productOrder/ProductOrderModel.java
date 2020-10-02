package com.malyszaryczlowiek.shop.productOrder;

import com.malyszaryczlowiek.shop.products.ProductModel;

import org.springframework.hateoas.RepresentationModel;

/**
 * DONE.
 *
 */
public class ProductOrderModel extends RepresentationModel<ProductOrderModel> {

    private final ProductModel productModel;
    private final Integer numberOfProducts;

    public ProductOrderModel(ProductOrder productOrder) {
        this.productModel = new ProductModel(productOrder.getProduct(), false);
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
