package com.malyszaryczlowiek.shop.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin")
public class AdminController {

    Logger logger = LoggerFactory.getLogger(AdminController.class);


    @RequestMapping(method = RequestMethod.GET)
    String welcomeAdminPanel() {
        return "Welcome Admin Panel";
    }

}





/*
Najważniejsze informacje o sesji:


 - Unieważnianie sesji
authentication.setAuthenticated(false); // WAŻNE to unieważnia sesję.

natomiast:
sessionStatus.setComplete(); // to nie unieważnia sesji
 */





























