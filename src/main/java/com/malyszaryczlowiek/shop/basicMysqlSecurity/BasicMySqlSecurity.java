package com.malyszaryczlowiek.shop.basicMysqlSecurity;

import com.malyszaryczlowiek.shop.client.ClientDetailsService;
import com.malyszaryczlowiek.shop.profiles.BasicMySqlAuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


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
        auth.userDetailsService(clientDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // wyłączam ochronę CSRF aby móc korzystać z Postmana
        //csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());//
        http.authorizeRequests()
                .antMatchers( "/admin/**", "/testing/**")
                .hasRole("ADMIN")
                .antMatchers("/myOrders/**", "/myAccount/**")// myOrders będą pod myAccount
                .hasRole("CLIENT")
                .antMatchers("/**").permitAll() //  "/createAccount", "/product", "/product/**", "/search", "/shoppingCart"
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

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}












