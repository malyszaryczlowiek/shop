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
 * @see RepresentationModel
 */
public class ClientModelForAdmin extends RepresentationModel<ClientModelForAdmin> {

    private final String email;
    private final Integer phone;
    private final String roles;
    private final boolean accountNotExpired;
    private final boolean accountNotLocked;
    private final boolean credentialsNotExpired;
    private final boolean isEnabled;

    public ClientModelForAdmin(Client client) {
        this.email = client.getEmail();
        this.phone = client.getPhone();
        this.roles = client.getRoles();
        this.accountNotExpired = client.isAccountNotExpired();
        this.accountNotLocked = client.isAccountNotLocked();
        this.credentialsNotExpired = client.isCredentialsNotExpired();
        this.isEnabled = client.isEnabled();
    }

    public String getEmail() {
        return email;
    }

    public Integer getPhone() {
        return phone;
    }

    public String getRoles() {
        return roles;
    }

    public boolean isAccountNotExpired() {
        return accountNotExpired;
    }

    public boolean isAccountNotLocked() {
        return accountNotLocked;
    }

    public boolean isCredentialsNotExpired() {
        return credentialsNotExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
