package com.malyszaryczlowiek.shop.products;

import com.malyszaryczlowiek.shop.brand.Brand;
import com.malyszaryczlowiek.shop.categories.Category;
import com.malyszaryczlowiek.shop.feature.Feature;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * Encja zawierająca podstawwowe dane jakie powinien zawierać każdy
 * produkt.
 *
 * WAŻNE!!!: tylko ostatnie klasy encji (oznaczone jako final? są klasami
 * które będziemy wyświetlali jako produkty na stronie, ale po wyższych
 * klasach można zrobić wyszukiwanie - chyba nie. lepiej robić wyszukiwanie
 * tylko w danej kategorii.
 *
 *
 * TODO można jeszcze dodać popularność
 * można jescze dodać komentarze w formie @ManyToOne
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "general_products_informations")
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", unique = true)
    private Long id;

    @ManyToOne
    private Feature bra;

    @ManyToOne
    private Feature prodName;



    @NotNull
    @ManyToOne
    @Column(name = "brand")
    private Brand brand;


    @NotEmpty
    @NotBlank
    @NotNull
    @Column(name = "product_name", unique = true, nullable = false)
    private String productName;


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

    /**
     *
     */
    @ManyToMany
    private List<Product> components;


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

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
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

// TODO można jeszcze zdefiniowac popularność jako liczbę sprzedanych sztuk w ciągu ostatniego miesiąca
// można też dodać parametr oznaczający promocję / nowość / ostatnie sztuki gdy jest mniej niż 5























/*
 * TODO zarówno description jak i specyfikacje można przenieść
 * do odpowienich klas i feczować tylko w przypadku gdy chcemy
 * otrzymać szczegółwowy opis produktu.
 *
 * opis dzielę na paragrafy oddzielone od siebie za pomocą {@literal <p>}
 * {@literal pargraf 1<<p>> paragraf2 } itd.
 */
//@Column(name = "product_description")
//private String description;


/*
 * specyfikacji będzie w postaci:<p>
 * {@literal cecha::wartość<<p>>cecha::wartość<<p>>}
 * parsując do mapy najpierw splitujemy po {@literal <<p>>}
 * a następnie do mapy  po "::"
 */
//@Column(name = "product_specification", length = 4096)
//private String specification;
