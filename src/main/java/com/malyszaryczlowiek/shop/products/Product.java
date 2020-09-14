package com.malyszaryczlowiek.shop.products;

import com.malyszaryczlowiek.shop.categories.Category;
import com.malyszaryczlowiek.shop.feature.Feature;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 *
 *
 * Poniżej stara dokumentacja
 *
 * Encja zawierająca podstawwowe dane jakie powinien zawierać każdy
 * produkt.
 *
 * WAŻNE!!!: tylko ostatnie klasy encji (oznaczone jako final? są klasami
 * które będziemy wyświetlali jako produkty na stronie, ale po wyższych
 * klasach można zrobić wyszukiwanie - chyba nie. lepiej robić wyszukiwanie
 * tylko w danej kategorii.
 *
 *
 * TOD można jeszcze dodać popularność
 * można jescze dodać komentarze w formie @ManyToOne
 *
 */
@Entity
//@Inheritance(strategy = InheritanceType.JOINED) // to było używane przy dziediczeniu po encjach.
@Table(name = "products")
public class Product { //extends Feature


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", unique = true)
    private Long id;


    /**
     * cascade: dodaję tylko Persist tak aby przy dodawaniu nowego
     * produktu, który nie pasuje jeszcze do żadnej kategorii,
     * żeby nowa kategoria od razu była dodwana do DB.
     * <p>
     * TOD sprawdzić jeszcze mergowanie i refreshowanie czy są bezpieczne.
     */
    @NotNull
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Category productCategory;

    // uwaga każdą encję która ma swój egzemplarz w bazie dancyh należy oznaczyć
    // CascadeType.PERSIST co oznacza, że jeśli dodajemy Product i nie ma tam jeszcze
    // danej kategorii to zostanie ona dodana automatycznie,
    // persist oznacza tutaj przechodzenie w stan zarządzany.

    // tutaj trzeba jeszcze wszędzie gdzie to potrzebne pouzupełniać popularność
    /*
    trzeba zrobić tak by popularnośc była obliczana na podstawie productOrder po id produktu
    trzeba dodać jeszcze do produkt ordr datę zamówienia. tak aby można było porównywac czas.
     */
    /**
     * popularność nie jest w żaden sposób wyświetlania, służy tylko i wyłącznie
     * w celach sortowania w ramach danej kategorii, lub sortowania w wynikach
     * wyszukiwania.
     */
    @Column(name = "popularity")
    private BigDecimal popularity;


    @NotNull
    @Column(name = "brand", nullable = false)
    private String brand;


    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;


    @NotNull
    @Column(name = "prize", nullable = false)
    private BigDecimal prize;


    @NotNull
    @Column(name = "amount", nullable = false)
    private Integer amount;


    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "product_feature_join_table",
            joinColumns = {@JoinColumn(name = "prod_id", referencedColumnName = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "feat_id", referencedColumnName = "feature_id")})
    @Fetch(FetchMode.SELECT)
    private final List<Feature> specification = new ArrayList<>();


    public Product() {}

    public Product(@NotNull Category productCategory, @NotNull String productBrand,
                   @NotNull String productName, @NotNull BigDecimal prize,
                   @NotNull Integer amountInStock) {
        this.productCategory = productCategory;
        this.brand = productBrand;
        this.productName = productName;
        this.prize = prize;
        this.amount = amountInStock;
        // this.components = components;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Category category) {
        this.productCategory = category;
    }

    public BigDecimal getPopularity() {
        return popularity;
    }

    public void setPopularity(BigDecimal popularity) {
        this.popularity = popularity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /*
    public List<Product> getComponents() {
        return components;
    }

    public void setComponents(List<Product> components) {
        this.components = components;
    }
     */

    public List<Feature> getSpecification() {
        return specification;
    }

    public void setSpecification(List<Feature> specification) {
        this.specification.clear();
        this.specification.addAll(specification);
    }
}













// TOD można jeszcze zdefiniowac popularność jako liczbę sprzedanych sztuk w ciągu ostatniego miesiąca
// można też dodać parametr oznaczający promocję / nowość / ostatnie sztuki gdy jest mniej niż 5


    /*
    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Feature productBrand;

    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Feature productName;

    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Feature prize;

    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Feature accessed;

    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Feature amountInStock;
     */



/*

// old implementanion

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

*/



    /*
    public Feature getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(Feature brand) {
        this.productBrand = brand;
    }

    public Feature getProductName() {
        return productName;
    }

    public void setProductName(Feature productName) {
        this.productName = productName;
    }

    public Feature getPrize() {
        return prize;
    }

    public void setPrize(Feature prize) {
        this.prize = prize;
    }

    public Feature getAccessed() {
        return accessed;
    }

    public void setAccessed(Feature accessed) {
        this.accessed = accessed;
    }

    public Feature getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(Feature amountInStock) {
        this.amountInStock = amountInStock;
    }
     */







/*
     * Z tego co pamiętam to trzeba zawsze zainicjlalizować listę
     *   tutaj nie oznaczam caskadowości bo inaczej zaciągał bym wszystkie produkty
     *  a lepiej jest je zaciągnąc tylko jak są nam niezbedne
     //
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(to implement)
    @Fetch(to choose)
    private List<Product> components = new ArrayList<>();
    */
















/*
 *  zarówno description jak i specyfikacje można przenieść
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
