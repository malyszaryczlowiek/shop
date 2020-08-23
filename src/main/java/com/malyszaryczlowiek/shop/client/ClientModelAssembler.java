package com.malyszaryczlowiek.shop.client;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


/**
 * Klasa Assemblująca (gromadząca, zbierająca, składująca)
 * model {@link ClientModel}
 */
public class ClientModelAssembler implements RepresentationModelAssembler<Client, ClientModel> {




    /**
     * Wyjaśnienie dlaczego nie warto używac RepresentationModelAssemblerSupport
     * tylko lepiej interface:
     * w tej metodzie trzeba samemu dodać linki bo przy użyciu metody
     * {@link RepresentationModelAssemblerSupport#createModelWithId(Object, Object)
     * createModelWithId(Object id, Object client)} wywala wyjątek związany z tym,
     * że {@link ClientModel} nie ma defaultowego konstruktora - a mieć nie może,
     * bo nie będziemy w stanie przypisać przy pomocy samych setterów
     * wartości do atrybutów tej klasy które są final.
     *
     * @param entity obiekt klasy Client który obudowujemy w linki
     * @return Obudowany obiekt w linki czyli obiekt klasy {@link ClientModel}
     */
    @Override
    public ClientModel toModel(Client entity) {
        ClientModel clientModel = new ClientModel(entity);
        return clientModel.add(
                linkTo(ClientAccountController.class).withSelfRel(),// to zwróci nam link do kontrolera czyli /myAccount _links._self.href
                linkTo(ClientAccountController.class).withRel("myAccount"), // to też zwróci link do kontrollera ale będzie on: _links.myAccount.href
                linkTo(ClientAccountController.class).withSelfRel().withName("withName")
                // dodać trzeba jeszcze linki do
                // 1. orderów
                // 2. koszyka

        );
    }


    /**
     * Metoda bierze j
     *
     * <p>
     * poprzez wywoałanie w metodzie add() metody toModel() z tej klasy
     *
     * @param entities najlepiej podawać listę
     * @return zwraca kolekcję z
     */
    @Override
    public CollectionModel<ClientModel> toCollectionModel(Iterable<? extends Client> entities) {
        int sizeOfCollection = 0;
        for (Client entity : entities)
            ++sizeOfCollection;

        Iterator<? extends Client> iterator = entities.iterator();
        List<ClientModel> list = new ArrayList<>(sizeOfCollection);
        while (iterator.hasNext())
            list.add(toModel(iterator.next()));
        return CollectionModel.of(list);
    }


}
















