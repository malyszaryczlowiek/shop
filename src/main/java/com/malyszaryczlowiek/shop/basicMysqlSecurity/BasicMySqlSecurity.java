package com.malyszaryczlowiek.shop.basicMysqlSecurity;

import com.malyszaryczlowiek.shop.client.ClientDetailsService;
import com.malyszaryczlowiek.shop.profiles.BasicMySqlAuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@BasicMySqlAuth
@Configuration
@EnableWebSecurity
public class BasicMySqlSecurity extends WebSecurityConfigurerAdapter {

    private final ClientDetailsService clientDetailsService;


    @Autowired
    public BasicMySqlSecurity(ClientDetailsService clientDetailsService){
        this.clientDetailsService = clientDetailsService;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(clientDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // wyłączam ochronę CSRF aby móc korzystać z Postmana
        //csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());//
        http.authorizeRequests()
                .antMatchers("/admin", "/admin/**", "/users/**",
                        "/testing/**").hasRole("ADMIN") // "/getClientAccount"
                .antMatchers("/myOrders","/myOrders/**", "/myAccount",
                        "/shoppingCart").hasRole("CLIENT")
                .antMatchers("/", "/createAccount", "/product",
                        "/product/**", "/search").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                //.formLogin()
                .and()
                .logout()
                //.addLogoutHandler(getSecurityContextLogoutHandler())
                //.logoutSuccessHandler()
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutUrl("/logMeOut") // do log outa powinni mieć dostęp tylko użytkownicy zalogowani?
                .logoutSuccessUrl("/")
                .permitAll();
    }
}












