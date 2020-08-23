package com.malyszaryczlowiek.shop.restMySqlSecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.malyszaryczlowiek.shop.profiles.RestMySqlAuth;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Zmieniam filtr tak aby zamiast odczytywać dane z formularza odczytywał dane z JSON'a
 */
@RestMySqlAuth
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /**
     * obiekt posłuży do mapowania danych z JSON'a na obiekt klasy
     * {@link LoginCredentials}, który przechowuje login i hasło
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Tworzymy obiekt {@code Autentication}, który następnie będzie wstrzykiwany/wykorzystany
     *
     */
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // przekształcam request w BufferReder
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;

            // wczytuję każdą linię i zapisuję ją do buildera
            while ((line = reader.readLine()) != null)
                sb.append(line);
            // mapuję dane z buildera do LoginCredentials
            LoginCredentials authRequest = objectMapper.readValue(sb.toString(), LoginCredentials.class);

            /*
            towrzę token będący kombinacją loginu i hasła. token ten będzie następnie wykorzystany
            do identyfikacji użytkownika (tzn. że jest on zautentyfikowany)
             */
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(),
                    authRequest.getPassword()
                    //new BCryptPasswordEncoder().encode(authRequest.getPassword())
            );
            System.out.println("hasło jest " + authRequest.getPassword() + " lub " + new BCryptPasswordEncoder().encode(authRequest.getPassword()));
            setDetails(request, token);

            return this.getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            e.getStackTrace();
            return null;
        }
    }
}
