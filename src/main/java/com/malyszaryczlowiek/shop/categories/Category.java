package com.malyszaryczlowiek.shop.categories;

import com.malyszaryczlowiek.shop.products.Product;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@Entity
@Table(name = "main_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", unique = true)
    private Long id;


    @NotEmpty
    @NotBlank
    @Column(name = "category_name", nullable = false)
    private String categoryName;


    @NotEmpty
    @NotBlank
    @Column(name = "1st_subcategory", nullable = false)
    private String subcategory1;


    @NotEmpty
    @NotBlank
    @Column(name = "2nd_subcategory", unique = true, nullable = false)
    private String subcategory2;


    /*
     * zostaję przy defaultowych ustawieniach gdzie
     * feching = Lazy a
     * orphanRemoval false
     * caskadowo nic nie robimy.

    @OneToMany(cascade = {})
    List<Product> listOfProductsInCategory;
    */


    public Category() {}

    public Category(@NotEmpty @NotBlank String categoryName,
                    @NotEmpty @NotBlank String subcategory1,
                    @NotEmpty @NotBlank String subcategory2) {
        this.categoryName = categoryName;
        this.subcategory1 = subcategory1;
        this.subcategory2 = subcategory2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubcategory1() {
        return subcategory1;
    }

    public void setSubcategory1(String subcategory1) {
        this.subcategory1 = subcategory1;
    }

    public String getSubcategory2() {
        return subcategory2;
    }

    public void setSubcategory2(String subcategory2) {
        this.subcategory2 = subcategory2;
    }
}
