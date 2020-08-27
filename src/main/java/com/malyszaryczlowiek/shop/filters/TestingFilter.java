package com.malyszaryczlowiek.shop.filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Aby stworzyć nowy filter trzeba zaimplementować interface {@link Filter}, oznaczyć jako
 * {@link Component} tak aby spring mógł go wczytać oraz jeśli dodajemy wiącej to używamy
 * jeszcze annotacji {@link org.springframework.core.annotation.Order} z arkumentem int.
 * <p></p>
 * W filtrze nadpisujemy metodą doFilter(ServletRequest, ServletResponse, FilterChain chain)
 * wewnątrz kórej należy wykonać {@code chain(request, response)}. tak aby kontynuować
 * wykonywanie łańcucha filtrów.
 * <p></p>
 * <b>Ważne</b>, tak zdefiniowany filter będzie przetwarzany
 * dla każdego rządania, dlatego aby ograniczyć liczbę ścieżek
 * dla których ma być on stosowany warto jest nadpisać
 * {@link org.springframework.boot.web.servlet.FilterRegistrationBean
 * FilterRegistrationBean}, tak jak to zrobiłem
 * w {@link TestingConfiguration#registerFilter()}.
 */
@Component
public class TestingFilter implements Filter {


    /**
     * w tej metodzie modyfikujemy wszysko co jest nam potrzebne.
     */
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        //if (httpServletRequest.getHeader("simpleHeader").equals("myHeader"))
        if (httpServletRequest.isUserInRole("ADMIN"))
            chain.doFilter(request, response);
        else // przekierowuje na stronę główną jeśli użytkownik nie ma roli ADMIN
            httpServletResponse.sendRedirect("/");

    }
}
