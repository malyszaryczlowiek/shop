package com.malyszaryczlowiek.shop.categories;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


/**
 *
 * <b>Deskryptory:</b> <p>
 * jeśli sekcja nosi nazwę: "Laptopy i komputery"
 * to jej deskryptor powinien wyglądać:
 * "laptopy_i_komputery"
 * <p>
 * W przypadku występowania polskich znaków należy
 * użyć znaku bez polskiej końcówki. Przykład:
 * "Podzespoły komputerowe" na
 * "podzespoly_komputerowe".
 */
@Entity
@Table(name = "category_table")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", unique = true)
    private Long id;


    @NotEmpty
    @NotBlank
    @Column(name = "section", nullable = false)
    private String section;


    @NotEmpty
    @NotBlank
    @Column(name = "section_descriptor", nullable = false)
    private String sectionDescriptor;


    @NotEmpty
    @NotBlank
    @Column(name = "category", nullable = false)
    private String category;


    @NotEmpty
    @NotBlank
    @Column(name = "category_descriptor", nullable = false)
    private String categoryDescriptor;


    @NotEmpty
    @NotBlank
    @Column(name = "subcategory", nullable = false)
    private String subcategory;


    @NotEmpty
    @NotBlank
    @Column(name = "subcategory_descriptor", nullable = false)
    private String subcategoryDescriptor;


    /*
     * zostaję przy defaultowych ustawieniach gdzie
     * feching = Lazy a
     * orphanRemoval false
     * caskadowo nic nie robimy.

    @OneToMany(cascade = {})
    List<Product> listOfProductsInCategory;
    */


    public Category() {}

    public Category(@NotEmpty @NotBlank String section, @NotEmpty @NotBlank String sectionDescriptor,
                    @NotEmpty @NotBlank String category, @NotEmpty @NotBlank String categoryDescriptor,
                    @NotEmpty @NotBlank String subcategory, @NotEmpty @NotBlank String subcategoryDescriptor) {
        this.section = section;
        this.sectionDescriptor = sectionDescriptor;
        this.category = category;
        this.categoryDescriptor = categoryDescriptor;
        this.subcategory = subcategory;
        this.subcategoryDescriptor = subcategoryDescriptor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String categoryName) {
        this.section = categoryName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String subcategory1) {
        this.category = subcategory1;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory2) {
        this.subcategory = subcategory2;
    }

    public String getSectionDescriptor() {
        return sectionDescriptor;
    }

    public void setSectionDescriptor(String sectionDescriptor) {
        this.sectionDescriptor = sectionDescriptor;
    }

    public String getCategoryDescriptor() {
        return categoryDescriptor;
    }

    public void setCategoryDescriptor(String categoryDescriptor) {
        this.categoryDescriptor = categoryDescriptor;
    }

    public String getSubcategoryDescriptor() {
        return subcategoryDescriptor;
    }

    public void setSubcategoryDescriptor(String subcategoryDescriptor) {
        this.subcategoryDescriptor = subcategoryDescriptor;
    }
}
