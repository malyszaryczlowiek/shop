package com.malyszaryczlowiek.shop.initializer;

import com.malyszaryczlowiek.shop.client.Client;
import com.malyszaryczlowiek.shop.client.ClientRepository;
import com.malyszaryczlowiek.shop.profiles.BasicMySqlAuth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Klasa konfiguracyjna, która ma za zadanie wypełnić DB użytkownikami
 */

@Configuration
@Order(1)
public class UserInitializer implements ApplicationRunner {

    private final ClientRepository clientRepository;
    private final Logger logger = LoggerFactory.getLogger(UserInitializer.class);

    @Autowired
    UserInitializer(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Wypełniam DB danymi ale tylko wtedy gdy tablica z użytkownikami jest pusta
     *
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializeDbEachTime();
    }

    @BasicMySqlAuth
    private void initializeDbEachTime() {

        Client admin = new Client();
        admin.setEmail("admin@gmail.com");
        admin.setPass(new BCryptPasswordEncoder().encode("adminadmin"));
        admin.setRoles("ROLE_ADMIN");

        Client user = new Client("client1@gmail.com",
                723796077,
                new BCryptPasswordEncoder().encode("useruseruser"));

        //Client userAdmin = new Client()

        clientRepository.deleteAll();
        clientRepository.save(admin);
        clientRepository.saveAndFlush(user);
        logger.debug("DB initialised with users");
    }
}
