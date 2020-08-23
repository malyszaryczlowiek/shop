package com.malyszaryczlowiek.shop.filters;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * prosta klasa konfiguracyjna mająca za zadanie dodać Bean
 * rejestrujący nowe filtry ale w taki sposób, że określamy dla
 * jakiej ścieżki ma działać ten filtr.
 * <p></p>
 * przykład wzięty z:<p>
 * <a href="https://www.baeldung.com/spring-boot-add-filter">
 *     baeldung.com/spring-boot-add-filter</a>
 */
@Configuration
public class TestingConfiguration {

    /**
     * Tworzę nowy rejestrator filtrów, dodaję do niego filtr,
     * następnie dodaję ścieżkę na jakiej ma ten filtr działać.
     *
     * @return bean który ma skonfigurowane, że Filtr będzie stosowany
     * tylko do ścieżek /testing/*
     */
    @Bean
    public FilterRegistrationBean<TestingFilter> registerFilter() {
        FilterRegistrationBean<TestingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TestingFilter());
        registrationBean.addUrlPatterns("/testing/*"); // filtr będzie działać tylko na tej ścieżce
        return registrationBean;
    }
}
