package com.malyszaryczlowiek.shop.feature;

import com.malyszaryczlowiek.shop.categories.Category;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Entity
//@Inheritance(strategy = InheritanceType.JOINED) // to było używane przy dziediczeniu po encjach.
@Table(name = "feature_table")
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feature_id", unique = true)
    private Long id;

    /**
     * kategoria musi być tutaj obecna tak aby nie dochodziło do kuriozów
     * w strylu: szukaj po procesorze dla komputerów i będzie wyszukiwało
     * po procesorze dla telefonów/urządzeń mobilnych.
     */
    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Category category;

    /**
     * Descryptor, który jest używany w wyszukiwaniu w URLu.
     */
    @NotEmpty
    @NotBlank
    @Column(name = "searching_descriptor", nullable = false)
    private String featureSearchingDescriptor; // eg. cpu, prize


    @NotEmpty
    @NotBlank
    @Column(name = "feature_name", nullable = false) // eg. Procesor, Prize
    private String featureName;


    @NotEmpty
    @NotBlank
    @Column(name = "feature_value", nullable = false)
    private String featureValue; // eg. i9-9600K, 4999.00


    /**
     * określa czy jest to podstawowa dana taka jak:
     * cena nazwa produktu brand, dostępność ilość w bazie danych.
     */
    @Column(name = "basic_information", nullable = false)
    private boolean basicInformation;


    /**
     * lista produktów która posiada daną cechę.
     * Na tę chwilę można z tego zrezygnować i posiłkować się repozytorium
     * w którym w SQL'a będziemy wstrzykiwali cechę którą będzie musiał znaleźć
     *
     */
    //@OneToMany
    //private List<Product> products;


    public Feature() {}

    public Feature(Category category, boolean basicInformation,
                   @NotEmpty @NotBlank String featureSearchingDescriptor,
                   @NotEmpty @NotBlank String featureName,
                   @NotEmpty @NotBlank String featureValue) {
        this.category = category;
        this.basicInformation = basicInformation;
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

    public boolean isBasicInformation() {
        return basicInformation;
    }

    public void setBasicInformation(boolean basicInformation) {
        this.basicInformation = basicInformation;
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
