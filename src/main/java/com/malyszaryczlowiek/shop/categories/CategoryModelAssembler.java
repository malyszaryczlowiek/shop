package com.malyszaryczlowiek.shop.categories;

import com.malyszaryczlowiek.shop.products.ProductController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Dla każdego obiektu kategorii generowany jest zestaw linków
 */
public class CategoryModelAssembler implements RepresentationModelAssembler<Category, CategoryModel> {

    @Override
    public CategoryModel toModel(Category entity) {
        CategoryModel model = new CategoryModel(entity);
        model.add(// link do kategorii
                linkTo(methodOn(ProductController.class)
                        .getAllCategoriesInSection(entity.getSectionDescriptor()))
                        .withSelfRel(),
                // link do podkategorii
                linkTo(methodOn(ProductController.class)
                        .getAllSubcategoriesInCategory(entity.getSectionDescriptor(), entity.getCategoryDescriptor()))
                        .withSelfRel().withRel(entity.getCategoryDescriptor()).withRel("sldgfj"));
        return model;
    }

    @Override
    public CollectionModel<CategoryModel> toCollectionModel(Iterable<? extends Category> entities) {
        int sizeOfCollection = 0;
        for (Category entity : entities) ++sizeOfCollection;
        Iterator<? extends Category> iterator = entities.iterator();
        List<CategoryModel> list = new ArrayList<>(sizeOfCollection);
        while (iterator.hasNext()) list.add(toModel(iterator.next()));
        return CollectionModel.of(list);
    }

}
