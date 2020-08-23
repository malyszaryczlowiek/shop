package com.malyszaryczlowiek.shop.products;

import com.malyszaryczlowiek.shop.categories.Category;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "general_products_informations")
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", unique = true)
    private Long id;


    @NotEmpty
    @NotBlank
    @NotNull
    @Column(name = "product_name", unique = true, nullable = false)
    private String productName;


    /**
     * TODO zarówno description jak i specyfikacje można przenieść
     * do odpowienich klas i feczować tylko w przypadku gdy chcemy
     * otrzymać szczegółwowy opis produktu.
     *
     * opis dzielę na paragrafy oddzielone od siebie za pomocą {@literal <p>}
     * {@literal pargraf 1<<p>> paragraf2 } itd.
     */
    //@Column(name = "product_description")
    //private String description;


    /**
     * specyfikacji będzie w postaci:<p>
     * {@literal cecha::wartość<<p>>cecha::wartość<<p>>}
     * parsując do mapy najpierw splitujemy po {@literal <<p>>}
     * a następnie do mapy  po "::"
     */
    //@Column(name = "product_specification", length = 4096)
    //private String specification;


    @Digits(integer=5, fraction=2, message = "Incorrect prize value.")
    @Column(name = "product_prize", nullable = false)
    private BigDecimal prize;


    @Column(name = "accessed", nullable = false)
    private boolean accessed = true;


    @PositiveOrZero
    @Digits(integer = 5, fraction = 0)
    @Column(name = "stock_amount", nullable = false)
    private Integer amountInStock = 1;

    /**
     * cascade: dodaję tylko Persist tak aby przy dodawaniu nowego
     * produktu, który nie pasuje jeszcze do żadnej kategorii,
     * żeby nowa kategoria od razu była dodwana do DB.
     * <p>
     * TODO sprawdzić jeszcze mergowanie i refreshowanie czy są bezpieczne.
     */
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @Column(name = "category", nullable = false)
    private Category category;


    public Product() {}

    public Product(String productName, BigDecimal prize, Category category) {
        this.productName = productName;
        this.prize = prize;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrize() {
        return prize;
    }

    public void setPrize(BigDecimal prize) {
        this.prize = prize;
    }

    public boolean isAccessed() {
        return accessed;
    }

    public void setAccessed(boolean accessed) {
        this.accessed = accessed;
    }

    public Integer getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(Integer amountInStock) {
        this.amountInStock = amountInStock;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
