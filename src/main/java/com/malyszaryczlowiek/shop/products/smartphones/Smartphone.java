package com.malyszaryczlowiek.shop.products.smartphones;

import com.malyszaryczlowiek.shop.products.Product;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "smartphones")
public class Smartphone extends Product {
}
