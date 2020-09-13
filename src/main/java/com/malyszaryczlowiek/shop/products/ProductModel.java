package com.malyszaryczlowiek.shop.products;

import com.malyszaryczlowiek.shop.feature.Feature;
import com.malyszaryczlowiek.shop.shoppingCart.ShoppingCartController;

import org.springframework.hateoas.RepresentationModel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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


    /**
     * Mapa musi być linkownaa aby zachować kolejność specyfikacji?
     */
    private final Map<String,String> specification = new LinkedHashMap<>();


    /**
     * To jest zestaw obowiązkowych pól, które zawsze będą musiały
     * się wyświetlić, gdy powbieramy informacje o produkcie i chemy
     * wyświetlić podstawowe info np w wynikach wyszukiwania.
     */
    private String brand;
    private String productName;
    private String prize;
    private String accessed;
    private String amountInStock;


    /**
     * to jest flaga, która oznacza, czy trzeba będzie wyświetlić
     * dodatkowe informacje o produkcie, będzie ona oznaczona jako true,
     * tylko w tedy gdy będziemy wchodzić na stronę produktu.
     * Jak wejdziemy na jego stronę to wywołamy na produkcie metodę
     * getSpecification(), która spowoduje zaciągnięcie dodatkowych
     * danych o specyfikacji z bazy danych. ale będzie się tak
     * działo tylko na stronie produktu dzięki czemu będziemy pobierali
     * informacje tylko o jednym produkcie i nie będziemy przez to
     * obciążali prawie wcale bazy danych.
     */
    private final boolean additionalSpecification;


    public ProductModel(Product product, boolean additionalSpecification) {
        this.additionalSpecification = additionalSpecification;
        this.section = product.getProductCategory().getSection();
        this.category = product.getProductCategory().getCategory();
        this.subcategory = product.getProductCategory().getSubcategory();

        // TODO to pilnie wymaga optymalizacji, bo streamowanie po kolei w celu znalezienia jednego
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


    /**
     * ustawia specyfikacje w taki sposób że do mapy ze specyfikacją dodawane są tylko informacje
     * o specyfikacji bez
     */
    private void setSpecification(Product product) {
        List<String> restrictedDescriptors = List.of("brand", "product_name", "prize",
                "accessed", "stock");
        product.getSpecification().forEach( feature -> {
            // jeśli nie zawiera już wczytanego feaczera to doaj go do specyfikacji
            // unikamy w ten sposób duplikowania w specyfikacji informacji o np. cenie itd.
            if ( !restrictedDescriptors.contains(feature.getFeatureSearchingDescriptor()) )
                specification.put(feature.getFeatureName(), feature.getFeatureValue());
        });
        // to jest dodatkowa specyfikacje, która będzie wyczytywana gdy wchodzimy
        // na stronę produktu
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
                    // todo to można/ trzeba zmodyfikować bo nie może być tak że podkategoria będzie kluczem
                });
            });
        }
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
                        .addProductToShoppingCart(entity, 0,10))
                        .withRel("shopping_cart").withName("add_product"),
                // link to usunięcia produktu jak zostanie dodany do koszyka ze strony produktu
                linkTo(methodOn(ShoppingCartController.class)
                        .removeProductFromShoppingCart(entity,0,10))
                        .withRel("shopping_cart").withName("remove_product"),
                // link do kategorii w której znajduje sie produkt
                linkTo(methodOn(ProductController.class).getAllProductsInSubcategory(
                        section, category, subcategory, 0, 20, "d", "popularity"))
                        .withRel("product_category")
        ));
    }


    public String getProductName() {
        return productName;
    }

    public String getBrand() {
        return brand;
    }

    public String getPrize() {
        return prize;
    }

    public String isAccessed() {
        return accessed;
    }

    public String getAccessed() {
        return accessed;
    }

    public String getAmountInStock() {
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
                // link do dodania produktu do koszyka ze strony produktu
                linkTo(methodOn(ProductController.class)
                        .putProductToShoppingCart(1, entity.getId()))
                        .withRel("shopping_cart").withName("add_and_redirect_to_product_page"),
                // link to usunięcia produktu jak zostanie dodany do koszyka ze strony produktu
                linkTo(methodOn(ProductController.class)
                        .deleteProductFromCart(entity.getId()))
                        .withRel("shopping_cart").withName("remove_and_redirect_to_product_page")
                 */

