package com.malyszaryczlowiek.shop.client;

import com.malyszaryczlowiek.shop.admin.AdminUserController;
import com.malyszaryczlowiek.shop.mainPageController.MainPageController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * Klasa Assemblująca (gromadząca, zbierająca, składująca)
 * model {@link ClientModel}
 */
public class ClientModelForAdminAssembler implements RepresentationModelAssembler<Client, ClientModelForAdmin> {



    /**
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
    public ClientModelForAdmin toModel(Client entity) {
        ClientModelForAdmin clientModelForAdmin = new ClientModelForAdmin(entity);
        return clientModelForAdmin.add(
                linkTo(AdminUserController.class).slash(entity.getId()).withSelfRel()
                ,linkTo(methodOn(AdminUserController.class).getFullUserInfo(entity.getId())).withSelfRel()
                ,linkTo(methodOn(AdminUserController.class).getFullUserInfo(entity.getId())).withRel("linkToThisFullUserInfo")
                ,linkTo(AdminUserController.class).slash(entity.getId()).withRel("LinkToSpecificUser")
                ,linkTo(AdminUserController.class).slash("allUsers").withRel("LinkWithHardCoded_allUsers")
                ,linkTo(methodOn(AdminUserController.class)
                        .getAllUsers(20,0,"desc", "name"))
                        .withRel("allUsers")
                ,linkTo(MainPageController.class).withRel("mainPage")
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
    public CollectionModel<ClientModelForAdmin> toCollectionModel(Iterable<? extends Client> entities) {
        Iterator<? extends Client> iterator = entities.iterator();
        List<ClientModelForAdmin> list = new ArrayList<>();
        while (iterator.hasNext())
            list.add(toModel(iterator.next()));
        return CollectionModel.of(list);
    }


}














