package com.malyszaryczlowiek.shop.client;


import com.malyszaryczlowiek.shop.converters.Converter;
import com.malyszaryczlowiek.shop.converters.ProductDescriptionConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import java.util.stream.Collectors;

//@Component //
 /**
  * nie może być @Component ponieważ nie wstzykujemy tutaj klienta.
  * jest to zwykła klasa POJO i jej obiekt nie jest przechowywany
  * w ApplicationContext ani w żadnym innym kontexście. Bo jest
  * tworzony na bierzącą z klasy {@link Client} której obiekt pobieramy
  * w konstruktorze. Służy ona tylko do wyekstrahowania Authorities
  * z Clienta.
*/
public class ClientDetails implements UserDetails {

    private final Logger logger = LoggerFactory.getLogger(ClientDetails.class);
    private final Client client;

    public ClientDetails(Client client) {
        this.client = client;
    }

     /**
      *
      * @return kolekcja zawierająca Authorieties czyli de facto ROLE
      * jakie ma użytkownik. Kolekcja jest niemodufikowalną listą.
      */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        logger.trace("Extracting GrantedAuthorities of user: " + client.toString());
        Converter converter = new ProductDescriptionConverter(",");
        return converter.convertFromStringToList(client.getRoles())
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableList());
    }

     //.map(role -> new SimpleGrantedAuthority(role))
     //return Collections.singletonList(new SimpleGrantedAuthority(client.getRole()));

     /**
      * ta metoda zwraca hasło w postaci BCrypt, które następnie będzie porównywane
      * do hasła, które zostanie przesłane z Basic Authentication w postmanie
      * i przy użyciu Beanu PasswordEncodera zdefiniowanego w klasie konfiguracyjnej
      * zostanie porównane.
      * @return hasło w postaci BCryptu
      */
    @Override
    public String getPassword() {
        return client.getPass();
    }

     /**
      * username to tak naprawdę email
      * @return email użytkowanika
      */
    @Override
    public String getUsername() {
        return client.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return client.isAccountNotExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return client.isAccountNotLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return client.isCredentialsNotExpired();
    }

    @Override
    public boolean isEnabled() {
        return client.isEnabled();
    }
}
