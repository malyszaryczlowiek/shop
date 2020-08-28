package com.malyszaryczlowiek.shop.products;

import com.malyszaryczlowiek.shop.feature.Feature;

import org.springframework.hateoas.RepresentationModel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @implSpec {@link RepresentationModel}
 * @see com.malyszaryczlowiek.shop.client.ClientModel ClientModel
 */
public class ProductModel extends RepresentationModel<ProductModel> {

    /*
    private final String productName;
    private final String brand;
    private final String prize;
    private final String accessed;

     */
    private final String section;
    private final String category;
    private final String subcategory;
    //private final String amountInStock;
    private final Map<String,String> specification = new LinkedHashMap<>();


    public ProductModel(Product product) {
        /*
        this.productName = product.getProductName().getFeatureValue();
        this.brand = product.getProductBrand().getFeatureValue();
        this.prize = product.getPrize().getFeatureValue();
        this.accessed = product.getAccessed().getFeatureValue();

         */
        this.section = product.getProductCategory().getSection();
        this.category = product.getProductCategory().getCategory();
        this.subcategory = product.getProductCategory().getSubcategory();
        //this.amountInStock = product.getAmountInStock().getFeatureValue();
        setSpecification(product);
    }

    private void setSpecification(Product product) {
        List<Feature> featureList = product.getSpecification();
        for(Feature feature: featureList)
            specification.put(feature.getFeatureName(), feature.getFeatureValue());
        /*
        List<Product> subProducts = product.getComponents();
        for (Product product1: subProducts)
            specification.put(product1.getProductName().getFeatureName(),
                    product1.getProductName().getFeatureValue());
         */

    }

    /*
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

    public String getAccessed() {
        return accessed;
    }

    public String getAmountInStock() {
        return amountInStock;
    }

     */



    public String getCategory() {
        return category;
    }



    public String getSection() {
        return section;
    }

    public String getSubcategory() {
        return subcategory;
    }


    public Map<String, String> getSpecification() {
        return specification;
    }
}
