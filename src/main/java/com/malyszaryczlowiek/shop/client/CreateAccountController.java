package com.malyszaryczlowiek.shop.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/createAccount")
public class CreateAccountController {

    private final Logger logger = LoggerFactory.getLogger(CreateAccountController.class);
    private final ClientRepository clientRepository;

    @Autowired
    public CreateAccountController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     *
     * @return zwraca pusty obiekt klienta, który w formularzu zostanie wypełniony
     * i przesłany z powrotem przy użyciu metody POST.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ClientCreator> createClient() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ClientCreator("youremail@email.com",123456789, "Your Password")
        );
    }


    /**
     * Metoda tworzy nowego Clienta.
     *
     * @param clientCreator obiekt klienta wysłany przez formularz po stronie front-endu
     * @return info o statusie.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> formWithClientData(@Valid @RequestBody ClientCreator clientCreator) {
        if (clientRepository.checkNumberOfClientWithThisEmail(clientCreator.getEmail()) > 0L){
            logger.warn("Client with this email already exists: " + clientCreator.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cannot create user with this email. " + clientCreator.getEmail() +
                            " is already taken.");
        } else {
            Client client = new Client(clientCreator.getEmail(), clientCreator.getPhone(),
                    new BCryptPasswordEncoder().encode(clientCreator.getPass()));
            clientRepository.saveAndFlush(client);
            logger.debug("client with email: " + clientCreator.getEmail() + " created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully.");
        }
    }
}
