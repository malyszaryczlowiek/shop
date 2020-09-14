package com.malyszaryczlowiek.shop.feature;

import com.malyszaryczlowiek.shop.products.Product;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;


@Entity
//@Inheritance(strategy = InheritanceType.JOINED) // to było używane przy dziediczeniu po encjach.
@Table(name = "features")
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feature_id", unique = true)
    private Long id;


    /**
     * Descryptor, który jest używany w wyszukiwaniu w URLu.
     * eg. cpu, prize, p_name
     */
    @NotEmpty
    @NotBlank
    @Column(name = "searching_descriptor", nullable = false)
    private String featureSearchingDescriptor; //


    /**
     * Wyświetlana nazwa cechy
     * eg. Processor, Prize, Product Name
     */
    @NotEmpty
    @NotBlank
    @Column(name = "feature_name", nullable = false) //
    private String featureName;

    /**
     *  eg. i9-9600K, 4999.00
     */
    @NotEmpty
    @NotBlank
    @Column(name = "feature_value", nullable = false)
    private String featureValue;


    /*
     * określa czy jest to podstawowa dana taka jak:
     * cena nazwa produktu brand, dostępność ilość w bazie danych.

    @Column(name = "basic_information", nullable = false)
    private boolean basicInformation;
    */

    /**
     * lista produktów która posiada daną cechę.
     * Na tę chwilę można z tego zrezygnować i posiłkować się repozytorium
     * w którym w SQL'a będziemy wstrzykiwali cechę którą będzie musiał znaleźć
     */
    @ManyToMany(mappedBy = "specification",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    // @JoinTable(to implement)
    // @Fetch(to choose)
    private final List<Product> products = new ArrayList<>();


    public Feature() {}

    public Feature(// boolean basicInformation, //Category category,
                   @NotEmpty @NotBlank String featureSearchingDescriptor,
                   @NotEmpty @NotBlank String featureName,
                   @NotEmpty @NotBlank String featureValue) {
        //this.category = category;
        // this.basicInformation = basicInformation;
        this.featureSearchingDescriptor = featureSearchingDescriptor;
        this.featureName = featureName;
        this.featureValue = featureValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*
    public boolean isBasicInformation() {
        return basicInformation;
    }

    public void setBasicInformation(boolean basicInformation) {
        this.basicInformation = basicInformation;
    }
     */

    public String getFeatureSearchingDescriptor() {
        return featureSearchingDescriptor;
    }

    public void setFeatureSearchingDescriptor(String featureSearchingDescriptor) {
        this.featureSearchingDescriptor = featureSearchingDescriptor;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getFeatureValue() {
        return featureValue;
    }

    public void setFeatureValue(String featureValue) {
        this.featureValue = featureValue;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products.addAll(products);
    }
}












/*
 * kategoria musi być tutaj obecna tak aby nie dochodziło do kuriozów
 * w strylu: szukaj po procesorze dla komputerów i będzie wyszukiwało
 * po procesorze dla telefonów/urządzeń mobilnych.
 * EDIT: Wyszukiwanie w ProductController nie wykorzystuje sprawdzania
 * kategorii wyłuskiwanej z Feature.
 */
    /*
    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Category category;
     */

/*
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
     */
