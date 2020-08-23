package com.malyszaryczlowiek.shop.client;


import com.malyszaryczlowiek.shop.clientUtil.PasswordChanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * kontroler do którego dostęp ma tylko użytkownik tak, że może:
 *
 * - sprawdzić swoje ostatnie zamówienia,
 * - sprawdzić swoje dane
 * - zmienić hasło (wymagane ponowne logowanie)
 * - dostać przekierowanie do koszyka ?
 */
@RestController
@RequestMapping("/myAccount")
public class ClientAccountController {

    private final Logger logger = LoggerFactory.getLogger(ClientAccountController.class);
    private final ClientRepository clientRepository;

    @Autowired
    public ClientAccountController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }



    /**
     * Wyłuskuję z sesji info o emailu użytkowanika
     * i odszukuję po nim użytkowanika w bazie danych.
     *
     * Filtr wyłapie, czy użytkownik o danej roli ma
     * dostęp do tej lokalizacji, Dlatego nie ma potrzeby
     * sprawdzania czy email != null bo nigdy null nie będzie
     *
     * @param authentication info o sesji
     * @return zwracam obiekt użytkownika
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ClientModel> getUserInfo(Authentication authentication) {
        String email = authentication.getName();
        Client client = clientRepository.findByEmail(email);
        logger.trace("Returning client information: " + email);
        ClientModelAssembler modelAssembler = new ClientModelAssembler();
        return ResponseEntity.status(HttpStatus.OK).body(modelAssembler.toModel(client));
    }


    /**
     * metoda w której najpierw wczytuję zwalidowany obiekt PasswordChanger
     * oraz Obiekt Authentication do wyciągnięcia info o zalogowanym użytkowniku,
     * Porównuję stary email z obecnym emailem w DB jeśli
     *
     * @param passwordChanger object with old and new password
     * @param authentication user object
     * @return Status message
     */
    @RequestMapping(method = RequestMethod.PATCH)
    ResponseEntity<String> changePassword(@Valid @RequestBody PasswordChanger passwordChanger,
                          Authentication authentication) {
        String email = authentication.getName();
        Client client = clientRepository.findByEmail(email);
        boolean oldPasswordMatcher = new BCryptPasswordEncoder()
                .encode(passwordChanger.getOldPass()).equals(client.getPass());
        if (oldPasswordMatcher) {
            client.setPass(new BCryptPasswordEncoder().encode(passwordChanger.getNewPass()));
            // Client refreshedClient =
                    clientRepository.saveAndFlush(client);
            //ClientModelAssembler modelAssembler = new ClientModelAssembler();
            logger.debug("Hasło zostało zmienione.");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Password changed.");
        }
        else {
            logger.debug("stare hasło z formularza nie pasuje do tego z DB");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Old email not matching");
        }
    }
}
















