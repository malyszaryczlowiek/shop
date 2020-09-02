package com.malyszaryczlowiek.shop.productOrder;


import com.malyszaryczlowiek.shop.products.Product;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.stream.Collectors;


@Entity
@Table(name = "product_order_table")
public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_order_id", unique = true)
    private Long id;


    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
    @NotNull
    private Product product;



    @Column(name = "number_of_ordered_products", nullable = false)
    @Positive
    private Integer numberOfOrderedProducts;



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


    public BigDecimal getProductOrderPrize() {
        // p oznacza tutaj prize
        String prize = product.getSpecification().stream()
                .filter(feature -> feature.getFeatureSearchingDescriptor().equals("prize"))
                .collect(Collectors.toList())
                .get(0)
                .getFeatureValue();
        BigDecimal productPrize = new BigDecimal(prize);
        BigDecimal totalPrize = new BigDecimal("0.00");
        for (int i = 0; i < numberOfOrderedProducts; i++)
            totalPrize = totalPrize.add(productPrize);
        //IntStream.range(0, numberOfOrderedProducts).collect((u) -> totalPrize, (i) -> totalPrize.add(productPrize), totalPrize )
        return new BigDecimal(prize);
    }
}


/*
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
    /*

    // tego muszę się pozbyć ponieważ jeśli będę chciał zmodyfikować
    // product order to zmodyfikuję też ten element dla innych orderów
    // co oznacza, że w zamówieniu innego użytkowanika pojawi się informacja
    // że zamawiał inną ilości niż to faktycznie miało miejsce.
    @ManyToMany(mappedBy = "listOfProducts",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Order> orders;
     */






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