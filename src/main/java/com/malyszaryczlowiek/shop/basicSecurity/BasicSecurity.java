package com.malyszaryczlowiek.shop.basicSecurity;

import com.malyszaryczlowiek.shop.profiles.BasicAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@BasicAuth
@Configuration
@EnableWebSecurity
public class BasicSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()//.passwordEncoder()
                .withUser("user")
                .password(new BCryptPasswordEncoder().encode("pass"))
                .roles("USER")
                .and()
                .withUser("admin")
                .password(new BCryptPasswordEncoder().encode("pass"))
                .roles("ADMIN");
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // wyłączam ochronę CSRF aby móc korzystać z Postmana
        //csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());//
        http.httpBasic().and().authorizeRequests()
                .antMatchers("/myAccount").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin", "/admin/**").hasRole("ADMIN")
                .antMatchers("/myOrders","/myOrders/**").hasRole("USER")
                .antMatchers("/", "/createAccount", "/product", "/product/**").permitAll()
                //.anyRequest().authenticated()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
    }
     */
}
