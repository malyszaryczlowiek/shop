package com.malyszaryczlowiek.shop.brand;

import com.malyszaryczlowiek.shop.products.Product;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;


    @Column(name = "brand_name", unique = true, nullable = false)
    private String brandName;


    @Column(name = "products")
    @OneToMany(mappedBy = "brand")
    private List<Product> products;






    public Brand() {}

    public Brand(String brandName, List<Product> products) {
        this.brandName = brandName;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
