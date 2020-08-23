package com.malyszaryczlowiek.shop.inMemorySecurity;


import com.malyszaryczlowiek.shop.profiles.InMemoryRestAuth;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@InMemoryRestAuth
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    /**
     * czyszczę informację o poprawnym zalogowaniu
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authentication) throws IOException, ServletException {
        clearAuthenticationAttributes(request);
    }
}
