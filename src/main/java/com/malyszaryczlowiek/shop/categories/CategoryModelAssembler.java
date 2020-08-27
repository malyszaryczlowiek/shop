package com.malyszaryczlowiek.shop.categories;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



/**
 * Dla każdego obiektu kategorii generowany jest zestaw linków
 */
public class CategoryModelAssembler implements RepresentationModelAssembler<List<Category>, CategoryModel> {

    private boolean subcategoriesFlag = false;

    @Override
    public CategoryModel toModel(List<Category> entity) {
        return new CategoryModel(entity, subcategoriesFlag);
    }

    public void setSubcategoriesLinksFlag(boolean flag) {
        this.subcategoriesFlag = flag;

    }

    @Override
    public CollectionModel<CategoryModel> toCollectionModel(Iterable<? extends List<Category>> entities) {
        int sizeOfCollection = 0;
        for (List<Category> entity : entities) ++sizeOfCollection;
        Iterator<? extends List<Category>> iterator = entities.iterator();
        List<CategoryModel> list = new ArrayList<>(sizeOfCollection);
        while (iterator.hasNext()) list.add(toModel(iterator.next()));
        return CollectionModel.of(list);
    }

}
