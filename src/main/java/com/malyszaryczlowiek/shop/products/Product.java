package com.malyszaryczlowiek.shop.products;

import com.malyszaryczlowiek.shop.categories.Category;
import com.malyszaryczlowiek.shop.feature.Feature;

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
 * TODO można jeszcze dodać popularność
 * można jescze dodać komentarze w formie @ManyToOne
 *
 */
@Entity
//@Inheritance(strategy = InheritanceType.JOINED) // to było używane przy dziediczeniu po encjach.
@Table(name = "general_products_informations")
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
     * TODO sprawdzić jeszcze mergowanie i refreshowanie czy są bezpieczne.
     */
    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Category productCategory;


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



    /**
     * Z tego co pamiętam to trzeba zawsze zainicjlalizować listę
     */
    @ManyToMany
    private List<Product> components = new ArrayList<>();



    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private final List<Feature> specification = new ArrayList<>();



    public Product() {}

    public Product(@NotNull Category productCategory, @NotNull Feature productBrand,
                   @NotNull Feature productName, @NotNull Feature prize,
                   @NotNull Feature accessed, @NotNull Feature amountInStock,
                   List<Product> components
    ) {
        this.productCategory = productCategory;
        this.specification.add(productBrand);
        this.specification.add(productName);
        this.specification.add(prize);
        this.specification.add(accessed);
        this.specification.add(amountInStock);
        this.components = components;
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

    public List<Product> getComponents() {
        return components;
    }

    public void setComponents(List<Product> components) {
        this.components = components;
    }

    public List<Feature> getSpecification() {
        return specification;
    }

    public void setSpecification(List<Feature> specification) {
        this.specification.clear();
        this.specification.addAll(specification);// = specification;
    }
}













// TODO można jeszcze zdefiniowac popularność jako liczbę sprzedanych sztuk w ciągu ostatniego miesiąca
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
