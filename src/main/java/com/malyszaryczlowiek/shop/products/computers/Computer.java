package com.malyszaryczlowiek.shop.products.computers;


import com.malyszaryczlowiek.shop.products.Product;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "computers")
public class Computer extends Product {

}
