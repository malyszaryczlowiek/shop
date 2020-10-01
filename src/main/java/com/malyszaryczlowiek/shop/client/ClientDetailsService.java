package com.malyszaryczlowiek.shop.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * Jest to service dostarczający w klasie konfigurującej bazpieczeństwo
 * tzn. {@link com.malyszaryczlowiek.shop.restMySqlSecurity.RestMySqlSecurity BasicH2Security} w metodzie
 * {@link com.malyszaryczlowiek.shop.restMySqlSecurity.RestMySqlSecurity#configure(AuthenticationManagerBuilder)
 * BasicH2Security.configure(AuthenticationManagerBuilder)}
 * do {@link org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
 * AuthenticationManagerBuilder}a informację o użytkowniku za pośrednictwem klasy
 * {@link ClientDetails}.
 * <p></p>
 * {@link UserDetailsService#loadUserByUsername(String)}
 *
 * @see UserDetailsService
 */
@Service
public class ClientDetailsService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(ClientDetailsService.class);
    private final ClientRepository clientRepository;


    /**
     * Konstruktor do którego wstrzykujemy Repozytorium z klientami.
     * @param clientRepository repozytorium (interface) {@link ClientRepository} extendujący
     *                         interface {@link org.springframework.data.jpa.repository.JpaRepository
     *                         JpaRepository<>}, które zawiera dodatkowo szereg nadpisanych metod
     *                         umożliwiających wyłuskanie innych danych (zazwyczaj po emailu,
     *                         który jest unikalny dla klażdego klienta).
     */
    public ClientDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * jedyna metoda którą trzeba nadpisać z interfejsu {@link UserDetailsService},
     * która ma za zadanie dostarczyć obiekt UserDetails. W tym obiekcie są
     * zapisane wszystkie dane odnośnie użytkowanika w tym hasło. oraz dane odnośnie
     * tego czy konto jest ważne zawieszone etc.
     * @param email email czyli login po którym wyszukujemy w repozytorium naszego
     *              klienta.
     * @return {@link UserDetails} - szczegółowe informacje o kliencie wyłuskane z klasy
     *              {@link Client} i zrzutowane na ten interface
     * @throws UsernameNotFoundException wyżucamy jak nie ma użytkownika o takim
     *              emailu.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmail(email);
        if (client != null) {
            logger.debug("znaleziono usera ");
            return new ClientDetails(client);
        }
        logger.error("usera: " + email + " nie znaloziono");
        throw new UsernameNotFoundException("Client with email '" + email + "' does not exists.");
    }
}
