package com.malyszaryczlowiek.shop.restMySqlSecurity;


import com.malyszaryczlowiek.shop.profiles.RestMySqlAuth;

/**
 * klasa przechowująca dane logowania. Password jest hashem? - sprawdzić.
 */
@RestMySqlAuth
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
