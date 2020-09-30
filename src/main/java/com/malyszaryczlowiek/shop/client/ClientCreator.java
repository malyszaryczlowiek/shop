package com.malyszaryczlowiek.shop.client;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;

public class ClientCreator {

    @Email
    private String email;

    @Digits(integer = 9, fraction = 0)
    private Integer phone;
    private String pass;

    public ClientCreator() {    }

    public ClientCreator(@Email String email, @Digits(integer = 9, fraction = 0) Integer phone, String pass) {
        this.email = email;
        this.phone = phone;
        this.pass = pass;
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

}
/*

    public Client getClient() {
        return new Client(email,phone,new BCryptPasswordEncoder().encode(pass));
    }
 */





















