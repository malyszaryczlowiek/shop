package com.malyszaryczlowiek.shop.restMySqlSecurity;

import com.malyszaryczlowiek.shop.profiles.RestMySqlAuth;
import com.malyszaryczlowiek.shop.client.ClientDetailsService;
import com.malyszaryczlowiek.shop.inMemorySecurity.MyUsernamePasswordAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RestMySqlAuth
@Configuration
@EnableWebSecurity
public class RestMySqlSecurity extends WebSecurityConfigurerAdapter {

    private final ClientDetailsService clientDetailsService;
    private final RestAuthenticationSuccessHandler successHandler;
    private final RestAuthenticationFailureHandler failureHandler;

    @Autowired
    public RestMySqlSecurity(ClientDetailsService clientDetailsService,
                             RestAuthenticationSuccessHandler successHandler,
                             RestAuthenticationFailureHandler failureHandler){
        this.clientDetailsService = clientDetailsService;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(clientDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // wyłączam ochronę CSRF aby móc korzystać z Postmana
        //csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());//
        http//.httpBasic().and()
                .authorizeRequests()
                .antMatchers("/admin", "/admin/**").hasRole("ADMIN")
                .antMatchers("/myAccount").hasAnyRole("USER", "ADMIN")
                .antMatchers("/myOrders","/myOrders/**").hasRole("USER")
                .antMatchers("/", "/createAccount", "/product", "/product/**").permitAll()
                //.anyRequest().authenticated()
                .and()
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .logout()
                .logoutUrl("/logout") // do log outa powinni mieć dostęp tylko użytkownicy zalogowani?
                .logoutSuccessUrl("/")
                .permitAll();
    }

    @Bean
    public UsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
        UsernamePasswordAuthenticationFilter filter = new MyUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        filter.setAuthenticationManager(super.authenticationManagerBean());
        return filter;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
