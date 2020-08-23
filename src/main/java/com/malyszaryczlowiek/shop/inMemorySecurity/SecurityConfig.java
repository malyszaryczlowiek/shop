package com.malyszaryczlowiek.shop.inMemorySecurity;


import com.malyszaryczlowiek.shop.profiles.InMemoryRestAuth;
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


@InMemoryRestAuth
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final RestAuthenticationSuccessHandler successHandler;
    private final RestAuthenticationFailureHandler failureHandler;


    SecurityConfig(RestAuthenticationSuccessHandler successHandler,
                   RestAuthenticationFailureHandler failureHandler) {
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // wyłączam ochronę CSRF aby móc korzystać z Postmana
        //csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());//
        http.authorizeRequests()
                .antMatchers("/myAccount").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin", "/admin/**").hasRole("ADMIN")
                .antMatchers("/myOrders","/myOrders/**").hasRole("USER")
                .antMatchers("/", "/createAccount", "/product", "/product/**").permitAll()
                //.anyRequest().authenticated()
                .and()
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .logout()
                .logoutSuccessUrl("/");
        //.httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("user").password("pass").roles("USER")
                .and()
                .withUser("admin").password("pass").roles("ADMIN");


        /*
        auth.inMemoryAuthentication().passwordEncoder(getPasswordEncoder())
                .withUser("user").password("pass").roles("USER")
                .and()
                .withUser("admin").password("pass").roles("ADMIN");
         */

        /*
        auth.inMemoryAuthentication()//.passwordEncoder()
                .withUser("user").password(getPasswordEncoder().encode("pass")).roles("USER")
                .and()
                .withUser("admin").password(getPasswordEncoder().encode("pass")).roles("ADMIN");
         */
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(); //BCryptPasswordEncoder();
    }


    // * Definiujemy filter który posłuży do Autentyfikacji czy istnieje dany user
    @Bean
    public UsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
        UsernamePasswordAuthenticationFilter filter = new MyUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        filter.setAuthenticationManager(super.authenticationManagerBean());
        return filter;
    }
}
