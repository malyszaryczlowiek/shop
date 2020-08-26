package com.malyszaryczlowiek.shop.feature;

import com.malyszaryczlowiek.shop.categories.Category;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


@Entity
@Table(name = "feature")
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    private Category category;


    @NotEmpty
    @NotBlank
    @Column(name = "searching_descriptor", nullable = false)
    private String featureSearchingDescriptor; // eg. cpu


    @NotEmpty
    @NotBlank
    @Column(name = "feature_name", nullable = false) // eg. Procesor
    private String featureName;


    @NotEmpty
    @NotBlank
    @Column(name = "feature_value", nullable = false)
    private String featureValue; // eg. i9-9600K


    /**
     * lista produktów która posiada daną cechę.
     * Na tę chwilę można z tego zrezygnować i posiłkować się repozytorium
     * w którym w SQL'a będziemy wstrzykiwali cechę którą będzie musiał znaleźć
     *
     */
    //@OneToMany
    //private List<Product> products;


    public Feature() {}

    public Feature(Category category,
                   @NotEmpty @NotBlank String featureSearchingDescriptor,
                   @NotEmpty @NotBlank String featureName,
                   @NotEmpty @NotBlank String featureValue) {
        this.category = category;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

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
}
