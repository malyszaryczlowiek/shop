package com.malyszaryczlowiek.shop.inMemorySecurity;


import com.malyszaryczlowiek.shop.profiles.InMemoryRestAuth;

/**
 * klasa przechowująca dane logowania. Password jest hashem? - sprawdzić.
 */
@InMemoryRestAuth
public class LoginCredentials {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
