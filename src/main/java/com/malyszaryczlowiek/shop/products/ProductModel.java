package com.malyszaryczlowiek.shop.products;

import com.malyszaryczlowiek.shop.mainPageController.MainPageController;
import com.malyszaryczlowiek.shop.shoppingCart.ShoppingCartController;

import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 *
 *
 * @implSpec {@link RepresentationModel}
 * @see com.malyszaryczlowiek.shop.client.ClientModel ClientModel
 */
public class ProductModel extends RepresentationModel<ProductModel> {


    /**
     * Zestaw obowiązkowych informacji o kategorii produktu.
     */
    private final String section;
    private final String category;
    private final String subcategory;
    private final String brand;
    private final String productName;
    private final BigDecimal prize;
    private final Integer amountInStock;


    /**
     * Mapa musi być linkownaa aby zachować kolejność specyfikacji?
     */
    private final Map<String,String> specification = new LinkedHashMap<>();


    /**
     *
     * @param product
     * @param additionalSpecification to jest flaga, która oznacza, czy trzeba będzie wyświetlić
     *       dodatkowe informacje o produkcie, będzie ona oznaczona jako true,
     *       tylko w tedy gdy będziemy wchodzić na stronę produktu.
     *       Jak wejdziemy na jego stronę to wywołamy na produkcie metodę
     *       getSpecification(), która spowoduje zaciągnięcie dodatkowych
     *       danych o specyfikacji z bazy danych. ale będzie się tak
     *       działo tylko na stronie produktu dzięki czemu będziemy pobierali
     *       informacje tylko o jednym produkcie i nie będziemy przez to
     *       obciążali prawie wcale bazy danych.
     */
    public ProductModel(Product product, boolean additionalSpecification) {
        this.section = product.getProductCategory().getSection();
        this.category = product.getProductCategory().getCategory();
        this.subcategory = product.getProductCategory().getSubcategory();
        this.brand = product.getBrand();
        this.productName = product.getProductName();
        this.prize = product.getPrize();
        this.amountInStock = product.getAmount();

        if (additionalSpecification) setSpecification(product);
        addLinks(product);
    }


    /**
     * ustawia specyfikacje w taki sposób że do mapy ze specyfikacją dodawane są tylko informacje
     * o specyfikacji bez
     */
    private void setSpecification(Product product) {
        product.getSpecification().forEach(
                feature -> specification.put(feature.getFeatureName(), feature.getFeatureValue())
        );
        // to jest dodatkowa specyfikacje, która będzie wyczytywana gdy wchodzimy
        // na stronę produktu
        /*
        if (additionalSpecification) {
            // logger.debug("inserting subproduct information to product model");
            product.getComponents().forEach(subProduct -> {
                Optional<Feature> subProductName = subProduct.getSpecification()
                        .stream()
                        .filter(spec -> spec.getFeatureName().equals("Product Name"))
                        .findFirst();
                subProductName.ifPresent(feature -> {
                    String subCategoryAsKey = subProduct.getProductCategory().getSubcategory();
                    specification.put(subCategoryAsKey, feature.getFeatureValue());
                    // to można/ trzeba zmodyfikować bo nie może być tak że podkategoria będzie kluczem
                });
            });
        }
         */
    }



    /**
     * metoda dodające linki:
     * <ul>
     *     <li> do strony produktu
     *     <li> link dodający produkt do koszyka
     *     <li> link do usunięcia produktu z koszyka (ze strony produktu)
     *     <li> link do kategorii w której znajduje się produkt
     * </ul>
     */
    private void addLinks(Product entity) {
        this.add(List.of(
                // link do strony produktu
                linkTo(methodOn(ProductController.class)
                        .getProduct(entity.getId()))
                        .withSelfRel(),
                // link do dodania produktu do koszyka ze strony produktu
                linkTo(methodOn(ShoppingCartController.class)
                        .addProductToShoppingCart(entity.getId()))
                        .withRel("put_to_shopping_cart").withType("PUT"),
                // link to usunięcia produktu jak zostanie dodany do koszyka ze strony produktu
                linkTo(methodOn(ShoppingCartController.class)
                        .removeProductFromShoppingCart(entity))
                        .withRel("delete_from_shopping_cart").withType("DELETE"),
                // link do kategorii w której znajduje sie produkt
                linkTo(methodOn(MainPageController.class).getAllProductsInSubcategoryWithPaging(
                        entity.getProductCategory().getSectionDescriptor(),
                        entity.getProductCategory().getCategoryDescriptor(),
                        entity.getProductCategory().getSubcategoryDescriptor(),
                        0, 20, "d", "popularity")).withRel("products_in_category")
        ));
    }


    public String getProductName() {
        return productName;
    }

    public String getBrand() {
        return brand;
    }

    public BigDecimal getPrize() {
        return prize;
    }

    public Integer getAmountInStock() {
        return amountInStock;
    }

    public String getCategory() {
        return category;
    }

    public String getSection() {
        return section;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public Map<String, String> getSpecification() {
        return specification;
    }
}





/*
public ProductModel(Product product, boolean additionalSpecification) {
        this.additionalSpecification = additionalSpecification;
        this.section = product.getProductCategory().getSection();
        this.category = product.getProductCategory().getCategory();
        this.subcategory = product.getProductCategory().getSubcategory();

        // ficzera jest nieoptymalne

        product.getSpecification().stream()
                .filter(feature -> feature.getFeatureSearchingDescriptor().equals("brand")) // b - brand
                .findAny()
                .ifPresent(feature ->  this.brand = feature.getFeatureValue());
        product.getSpecification().stream()
                .filter(feature -> feature.getFeatureSearchingDescriptor().equals("product_name")) // product Name
                .findAny()
                .ifPresent(feature ->  this.productName = feature.getFeatureValue());
        product.getSpecification().stream()
                .filter(feature -> feature.getFeatureSearchingDescriptor().equals("prize"))
                .findAny()
                .ifPresent(feature ->  this.prize = feature.getFeatureValue());
        product.getSpecification().stream()
                .filter(feature -> feature.getFeatureSearchingDescriptor().equals("accessed")) // a - accessed
                .findAny()
                .ifPresent(feature ->  this.accessed = feature.getFeatureValue());
        product.getSpecification().stream()
                .filter(feature -> feature.getFeatureSearchingDescriptor().equals("stock")) // sta - stock amount
                .findAny()
                .ifPresent(feature ->  this.amountInStock = feature.getFeatureValue());
        setSpecification(product);
        addLinks(product);
    }
 */




                /*
                // link do dodania produktu do koszyka ze strony produktu
                linkTo(methodOn(ProductController.class)
                        .putProductToShoppingCart(1, entity.getId()))
                        .withRel("shopping_cart").withName("add_and_redirect_to_product_page"),
                // link to usunięcia produktu jak zostanie dodany do koszyka ze strony produktu
                linkTo(methodOn(ProductController.class)
                        .deleteProductFromCart(entity.getId()))
                        .withRel("shopping_cart").withName("remove_and_redirect_to_product_page")
                 */

