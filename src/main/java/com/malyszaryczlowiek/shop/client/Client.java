package com.malyszaryczlowiek.shop.client;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import java.io.Serializable;


@Entity
@Table(name = "clients")
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    /**
     * email jest tutaj równocześnie loginem
     */
    @Column(name = "email", unique = true, nullable = false)
    @Email
    private String email;


    @Column(name = "phone")
    @Digits(integer = 9, fraction = 0)
    //@Pattern(regexp = "[0-9]{9}", message = "Invalid phone number")
    private Integer phone;


    //@Pattern(regexp = ".{9,}", message = "Password must have nine characters at least.")
    // nie ma potrzeby walidacji hasła pod wzgędem ilości znaków ponieważ i tak
    // zawsze przechowywany jest hash hasła
    @Column(name = "pass", nullable = false)
    private String pass;

    /*
    poniżej są pola potrzbne w Clasie UserDetails
    */

    /**
     * Defaultowo ustawiam role na CLIENT
     */
    @Column(name = "roles", nullable = false)
    private String roles = "ROLE_CLIENT";

    @Column(name = "account_not_expired", nullable = false)
    private boolean accountNotExpired = true;

    @Column(name = "account_not_locked", nullable = false)
    private boolean accountNotLocked = true;

    @Column(name = "credentials_not_expired", nullable = false)
    private boolean credentialsNotExpired = true;

    @Column(name = "is_enabled")
    private boolean isEnabled = true;


    public Client() {}

    public Client(@Email String email,
                  @Digits(integer = 9, fraction = 0) Integer phone,
                  String pass) {
        this.email = email;
        this.phone = phone;
        this.pass = pass;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(@Digits(integer = 9, fraction = 0) Integer phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean isAccountNotExpired() {
        return accountNotExpired;
    }

    public void setAccountNotExpired(boolean accountNotExpired) {
        this.accountNotExpired = accountNotExpired;
    }

    public boolean isAccountNotLocked() {
        return accountNotLocked;
    }

    public void setAccountNotLocked(boolean accountNotLocked) {
        this.accountNotLocked = accountNotLocked;
    }

    public boolean isCredentialsNotExpired() {
        return credentialsNotExpired;
    }

    public void setCredentialsNotExpired(boolean credentialsNotExpired) {
        this.credentialsNotExpired = credentialsNotExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public String toString() {
        return "Client[" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                ", pass='" + pass + '\'' +
                ", role='" + roles + '\'' +
                ", accountNotExpired=" + accountNotExpired +
                ", accountNotLocked=" + accountNotLocked +
                ", credentialsNotExpired=" + credentialsNotExpired +
                ", isEnabled=" + isEnabled +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client that = (Client) o;

        if (accountNotExpired != that.accountNotExpired) return false;
        if (accountNotLocked != that.accountNotLocked) return false;
        if (credentialsNotExpired != that.credentialsNotExpired) return false;
        if (isEnabled != that.isEnabled) return false;
        if (!id.equals(that.id)) return false;
        if (!email.equals(that.email)) return false;
        if (!phone.equals(that.phone)) return false;
        if (!pass.equals(that.pass)) return false;
        return roles.equals(that.roles);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + phone.hashCode();
        result = 31 * result + pass.hashCode();
        result = 31 * result + roles.hashCode();
        result = 31 * result + (accountNotExpired ? 1 : 0);
        result = 31 * result + (accountNotLocked ? 1 : 0);
        result = 31 * result + (credentialsNotExpired ? 1 : 0);
        result = 31 * result + (isEnabled ? 1 : 0);
        return result;
    }
}
