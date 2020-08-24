package com.malyszaryczlowiek.shop.products;

import org.springframework.hateoas.RepresentationModel;

import java.util.Map;

/**
 *
 *
 * @implSpec {@link RepresentationModel}
 * @see com.malyszaryczlowiek.shop.client.ClientModel ClientModel
 */
public class ProductModel extends RepresentationModel<ProductModel> {

    private final String productName;
    private final String brand;
    private final String prize;
    private final boolean accessed;
    private final String section;
    private final String category;
    private final String subcategory;
    private final Integer amountInStock;

    private Map<String, String> mapOfSpecification;


    public ProductModel(Product product) {
        this.productName = product.getProductName();
        this.brand = product.getBrand().getBrandName();
        this.prize = product.getPrize().toPlainString();
        this.accessed = product.isAccessed();
        this.section = product.getCategory().getSection();
        this.category = product.getCategory().getCategory();
        this.subcategory = product.getCategory().getSubcategory();
        this.amountInStock = product.getAmountInStock();
    }

    public String getProductName() {
        return productName;
    }

    public String getBrand() {
        return brand;
    }

    public String getPrize() {
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

    public String getSection() {
        return section;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public Map<String, String> getMapOfSpecification() {
        return mapOfSpecification;
    }

    public void setMapOfSpecification(Map<String, String> mapOfSpecification) {
        this.mapOfSpecification = mapOfSpecification;
    }
}
