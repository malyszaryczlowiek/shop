package com.malyszaryczlowiek.shop.categories;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;



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
    @Column(name = "category", nullable = false)
    private String category;


    @NotEmpty
    @NotBlank
    @Column(name = "subcategory", unique = true, nullable = false)
    private String subcategory;


    /*
     * zostaję przy defaultowych ustawieniach gdzie
     * feching = Lazy a
     * orphanRemoval false
     * caskadowo nic nie robimy.

    @OneToMany(cascade = {})
    List<Product> listOfProductsInCategory;
    */


    public Category() {}

    public Category(@NotEmpty @NotBlank String section,
                    @NotEmpty @NotBlank String category,
                    @NotEmpty @NotBlank String subcategory) {
        this.section = section;
        this.category = category;
        this.subcategory = subcategory;
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
}
