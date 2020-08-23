package com.malyszaryczlowiek.shop.client;

import org.springframework.hateoas.RepresentationModel;

/**
 * W HATEOAS'ie z klasy @Entity {@link Client} budujemy model oparty
 * o klasę encji. Ale biorę tutaj tylko dane które nie są wrażliwe.
 * Nie wysyłam info o tym czy client jest zawieszony ani jaki jest
 * hash jego hasła.
 * <p>
 * WAŻNE: Nie ma potrzeby wyciągania z klienta id i wstawiania go do
 * oddzielnego atrybutu, bo nie jest ono nigdzie potrzebne. Jak będziemy
 * chcieli dostać się do clienta, to mamy stworzony przez HATEOAS link.
 *
 * @see org.springframework.hateoas.RepresentationModel
 */
public class ClientModel extends RepresentationModel<ClientModel> {

    private final String email;
    private final Integer phone;

    public ClientModel(Client client) {
        this.email = client.getEmail();
        this.phone = client.getPhone();
    }

    public String getEmail() {
        return email;
    }

    public Integer getPhone() {
        return phone;
    }
}
