package com.malyszaryczlowiek.shop.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/admin")
public class AdminMainController {

    /**
     * TODO metoda musi zwrócić linki do:
     * - listy kategorii
     * - edycji kategori wyszukanej po id
     * - usunięcia kategorii
     *
     * - do listy produktów w kategoriach
     * - edycji
     * - usunięcia.
     *
     *
     * @return zwracam linki do panelu
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<AdminPanelLinkProvider> welcomeAdminPanel() {
        return ResponseEntity.status(HttpStatus.OK).body(new AdminPanelLinkProvider());
    }


}
