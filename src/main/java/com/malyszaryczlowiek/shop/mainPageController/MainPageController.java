package com.malyszaryczlowiek.shop.mainPageController;


import com.malyszaryczlowiek.shop.client.Client;
import com.malyszaryczlowiek.shop.client.ClientCreator;
import com.malyszaryczlowiek.shop.client.ClientRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/")
public class MainPageController {

    private final Logger logger = LoggerFactory.getLogger(MainPageController.class);
    private final ClientRepository clientRepository;
    //private final MainPageLinkSupplier mainPageLinkSupplier;


    public MainPageController(ClientRepository clientRepository
//            ,                              MainPageLinkSupplier mainPageLinkSupplier
    ) {
        this.clientRepository = clientRepository;
      //  this.mainPageLinkSupplier = mainPageLinkSupplier;
    }


    /**
     * Strona startowa aplikacji
     * @return powitanie
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<MainPageModel> welcomePage() {
        MainPageLinkSupplier mainPageLinkSupplier = new MainPageLinkSupplier();
        MainPageModelAssembler assembler = new MainPageModelAssembler(mainPageLinkSupplier);
        MainPage main = new MainPage();
        return ResponseEntity.status(HttpStatus.OK).body(assembler.toModel(main));
    }


    /**
     *
     * @return zwraca pusty obiekt klienta, który w formularzu zostanie wypełniony
     * i przesłany spowrotem przy użyciu metody POST.
     */
    @RequestMapping(path = "/createAccount", method = RequestMethod.GET)
    public ResponseEntity<ClientCreator> createClient() {
        logger.trace("MainController.createClient(): Empty Client JSON object send");
        return ResponseEntity.status(HttpStatus.OK).body(new ClientCreator());
    }


    /**
     * Metoda tworzy nowego Clienta.
     *
     * @param clientCreator obiekt klienta wysłany przez formularz po stronie front-endu
     * @return info o statusie.
     */
    @RequestMapping(path = "/createAccount", method = RequestMethod.POST)
    public ResponseEntity<String> formWithClientData(@Valid @RequestBody ClientCreator clientCreator) {
        if (clientRepository.checkNumberOfClientWithThisEmail(clientCreator.getEmail()) > 0L){
            logger.warn("Client with this email already exists: " + clientCreator.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot create user with this email. " +
                    clientCreator.getEmail() +
                    " is already taken.");
        } else {
            Client client = clientCreator.getClient();
            clientRepository.saveAndFlush(client);
            logger.trace("client with email: " + clientCreator.getEmail() + " created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully.");
        }
    }
}












/*
    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    void logout(HttpServletRequest request,
                HttpServletResponse response,
                Authentication authentication) throws IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cookie: cookies)
                cookie.setMaxAge(0);

        HttpSession session = request.getSession(false);
        if (session != null) {
            logger.debug("Invalidating session: " + session.getId());
            session.invalidate();
        }
        authentication.setAuthenticated(false);
        response.sendRedirect("/");
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(null);
		SecurityContextHolder.clearContext();
		logger.debug("proces wylogowania zakończony");
    }
 */