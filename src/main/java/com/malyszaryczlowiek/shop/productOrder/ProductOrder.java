package com.malyszaryczlowiek.shop.productOrder;

import com.malyszaryczlowiek.shop.order.Order;
import com.malyszaryczlowiek.shop.products.Product;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "product_order_table")
public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_order_id", unique = true)
    private Long id;

/*
    @NotBlank
    @NotEmpty
    @Column(name = "product_category", nullable = false)
    private String productCategory;



     * włąściwy produkt zaciągnie się w ProductOrderModel

    @PositiveOrZero
    @Column(name = "product_id", nullable = false)
    private Long productId;
 */


    /*
     * tutaj feczowanie jesty defaultowo ustawione na true dzieęki czemu pobierając
     * ProductOrder, pobieramy też odpowiedni Product.<p></p>
     *
*/
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @NotNull
    private Product product;

    @Column(name = "number_of_ordered_products", nullable = false)
    @Positive
    private Integer numberOfOrderedProducts;


    /**
     * właścicielem relacji jest Order dlatego podajemy parametr mappedBy
     * a jego wartością jest nazwa atrybutu klasy będącej właścicielem
     * (Order).<p></p>
     * Feczujemy leniwie ponieważ zaciągając Product order nie chcemy aby
     * pobierał wszczystkie Ordery w jakich był wykorzstywany.<p></p>
     * TODO sprawdzić jak powinno być jeszcze zrobione operacje kaskadowe.
     * W przypadku cascadingu nie możemy zrobić Detached ponieważ odłączało
     * by to również Order a Order zawiera przecież też i inne ProductOrdery
     *
     */
    @ManyToMany(mappedBy = "listOfProducts",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Order> orders;





    public ProductOrder () {}

    public ProductOrder(Product product, Integer numberOfOrderedProducts) {
        this.product = product;
        this.numberOfOrderedProducts = numberOfOrderedProducts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getNumberOfOrderedProducts() {
        return numberOfOrderedProducts;
    }

    public void setNumberOfOrderedProducts(Integer numberOfOrderedProducts) {
        this.numberOfOrderedProducts = numberOfOrderedProducts;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
