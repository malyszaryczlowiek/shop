package com.malyszaryczlowiek.shop.products;

import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

/**
 *
 *
 * @implSpec {@link RepresentationModel}
 * @see com.malyszaryczlowiek.shop.client.ClientModel ClientModel
 */
public class ProductModel extends RepresentationModel<ProductModel> {

    private final String productName;
    private final BigDecimal prize;
    private final boolean accessed;
    private final String category;
    private final Integer amountInStock;


    public ProductModel(Product product) {
        this.productName = product.getProductName();
        this.prize = product.getPrize();
        this.accessed = product.isAccessed();
        this.category = product.getCategory().getCategoryName();
        this.amountInStock = product.getAmountInStock();
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getPrize() {
        return prize;
    }

    public boolean isAccessed() {
        return accessed;
    }

    public String getCategory() {
        return category;
    }

    public Integer getAmountInStock() {
        return amountInStock;
    }
}
