package com.malyszaryczlowiek.shop.model;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 *
 * @param <E> parametr encji
 */
@Deprecated
public abstract class GeneralModelAssemblerAbstraction<E> implements RepresentationModelAssembler<E, GeneralModel> {

    @Override
    public GeneralModel toModel(E entity) {
        GeneralModel model = new GeneralModel();
        model.add(setLinks());
        return model;
    }

    @Override
    public CollectionModel<GeneralModel> toCollectionModel(Iterable<? extends E> entities) {
        int sizeOfCollection = 0;
        for (E entity : entities) ++sizeOfCollection;
        Iterator<? extends E> iterator = entities.iterator();
        List<GeneralModel> list = new ArrayList<>(sizeOfCollection);
        while (iterator.hasNext()) list.add(toModel(iterator.next()));
        return CollectionModel.of(list);
    }

    /**
     * metoda którą trzeba zaimplementować w konkretnym Model Assemblerze.
     * Każdy implementujący ModelAssembler
     *
     * @return lista linków, które mają być przypisane dla danego modelu.
     */
    protected abstract List<Link> setLinks();
}
