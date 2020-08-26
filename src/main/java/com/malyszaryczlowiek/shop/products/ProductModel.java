package com.malyszaryczlowiek.shop.products;

import org.springframework.hateoas.RepresentationModel;

import java.util.LinkedHashMap;
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
    private final String accessed;
    private final String section;
    private final String category;
    private final String subcategory;
    private final String amountInStock;

    private Map<String, String> mapOfSpecification = new LinkedHashMap<>();


    public ProductModel(Product product) {
        this.productName = product.getProductName().getFeatureValue();
        this.brand = product.getBrand().getFeatureValue();
        this.prize = product.getPrize().getFeatureValue();
        this.accessed = product.getAccessed().getFeatureValue();
        this.section = product.getCategory().getSection();
        this.category = product.getCategory().getCategory();
        this.subcategory = product.getCategory().getSubcategory();
        this.amountInStock = product.getAmountInStock().getFeatureValue();
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

    public String isAccessed() {
        return accessed;
    }

    public String getCategory() {
        return category;
    }

    public String getAmountInStock() {
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
