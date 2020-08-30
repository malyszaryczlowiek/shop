package com.malyszaryczlowiek.shop.products;

import com.malyszaryczlowiek.shop.feature.Feature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.RepresentationModel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 *
 * @implSpec {@link RepresentationModel}
 * @see com.malyszaryczlowiek.shop.client.ClientModel ClientModel
 */
public class ProductModel extends RepresentationModel<ProductModel> {

    private final Logger logger = LoggerFactory.getLogger(ProductModel.class);

    private final String section;
    private final String category;
    private final String subcategory;
    private final boolean additionalSpecification;
    private final Map<String,String> specification = new LinkedHashMap<>();

    private String brand;
    private String productName;
    private String prize;
    private String accessed;
    private String amountInStock;


    public ProductModel(Product product, boolean additionalSpecification) {
        logger.debug("inserting Data from Product to ProductModel");
        this.additionalSpecification = additionalSpecification;
        this.section = product.getProductCategory().getSection();
        this.category = product.getProductCategory().getCategory();
        this.subcategory = product.getProductCategory().getSubcategory();
        product.getSpecification().stream()
                .filter(feature -> feature.getFeatureSearchingDescriptor().equals("b")) // b - brand
                .findAny()
                .ifPresent(feature ->  this.brand = feature.getFeatureValue());
        product.getSpecification().stream()
                .filter(feature -> feature.getFeatureSearchingDescriptor().equals("pn"))
                .findAny()
                .ifPresent(feature ->  this.productName = feature.getFeatureValue());
        product.getSpecification().stream()
                .filter(feature -> feature.getFeatureSearchingDescriptor().equals("p"))
                .findAny()
                .ifPresent(feature ->  this.prize = feature.getFeatureValue());
        product.getSpecification().stream()
                .filter(feature -> feature.getFeatureSearchingDescriptor().equals("a")) // a - accessed
                .findAny()
                .ifPresent(feature ->  this.accessed = feature.getFeatureValue());
        product.getSpecification().stream()
                .filter(feature -> feature.getFeatureSearchingDescriptor().equals("sta")) // sta - stock amount
                .findAny()
                .ifPresent(feature ->  this.amountInStock = feature.getFeatureValue());
        setSpecification(product);
    }

    private void setSpecification(Product product) {
        List<Feature> featureList = product.getSpecification();
        for(Feature feature: featureList)
            specification.put(feature.getFeatureName(), feature.getFeatureValue());
        // TODO sprawdzić czy trzeba to
        if (additionalSpecification) {
            logger.debug("inserting subproduct information to product model");
            List<Product> subProducts = product.getComponents();
            for (Product subProduct: subProducts) {
                Optional<Feature> subProductName = subProduct.getSpecification()
                        .stream()
                        .filter(spec -> spec.getFeatureName().equals("Product Name"))
                        .findFirst();
                subProductName.ifPresent(feature ->
                        specification.put(feature.getFeatureName(), feature.getFeatureValue()));
            }
        }
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

    public String getAccessed() {
        return accessed;
    }

    public String getAmountInStock() {
        return amountInStock;
    }

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
