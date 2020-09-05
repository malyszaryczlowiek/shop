package com.malyszaryczlowiek.shop.order;

import com.malyszaryczlowiek.shop.client.Client;
import com.malyszaryczlowiek.shop.feature.Feature;
import com.malyszaryczlowiek.shop.productOrder.ProductOrder;
import com.malyszaryczlowiek.shop.products.Product;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Zamówienie jest obiektem któy został zrealizowany. Tzn. jest to operacja
 * zakończona.
 *
 * order powinien być nieusuwalny z bazy danych.
 *
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    /**
     * fechowanie ustawiamy na Lazy ponieważ nie ma potyrzeby by
     * zaciągać informacji o kliencie przy pobieraniu danych
     * o zamówieniu.<p></p>
     * Nie ustawiam caskadingu bo nie ma znaczenia sensu przy
     * jakiejkowlwiek zmianie usówania ani.
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;


    /**
     * Feczowanie musi być Eager bo zaciągając Order powinniśmy zaciągnąć
     * zarówno ProductOrdery jak i zawierane przez nie Producty. <p></p>
     * Caskading chyba tylko trzeba refreshing bo Usuwając Order nie
     * powinniśmy usówać ani ProductOrderów ani produktów się w nich
     * znajdujących.
     *
     */
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.REFRESH})
    @Fetch(FetchMode.SELECT)
    private final List<ProductOrder> listOfProducts = new ArrayList<>();


    /**
     * możliwe do przyjęcia statusy to:
     * Completed, In progress, Cancelled,
     */
    @Column(name = "status", nullable = false)
    private String status;


    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;



    public Order() {}

    public Order(List<ProductOrder> productOrders , String status) {
        this.listOfProducts.addAll(productOrders);
        this.status = status;
    }

    public String getTotalPrize() {
        BigDecimal totalPrize = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        for (ProductOrder productOrder : listOfProducts) {
            List<Feature> l = productOrder.getProduct().getSpecification().stream().filter(
                    feature -> feature.getFeatureSearchingDescriptor().equals("prize")).collect(Collectors.toList());
            //new BigDecimal(productOrder.getProduct().getPrize().getFeatureValue());
            BigDecimal productPrize = new BigDecimal(l.get(0).getFeatureValue());
            for (int i  = 0; i < productOrder.getNumberOfOrderedProducts(); ++i )
                totalPrize.add(productPrize);
        }
        return totalPrize.toPlainString();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<ProductOrder> getListOfProducts() {
        return listOfProducts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime dateOfOrder) {
        this.orderDate = dateOfOrder;
    }

    // todo to reimplement?
    public void setListOfProducts(List<ProductOrder> listOfProducts) {
        this.listOfProducts.clear();
        this.listOfProducts.addAll(listOfProducts);
    }
}

























