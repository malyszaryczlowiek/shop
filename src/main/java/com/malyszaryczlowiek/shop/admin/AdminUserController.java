package com.malyszaryczlowiek.shop.admin;

import com.malyszaryczlowiek.shop.client.Client;
import com.malyszaryczlowiek.shop.client.ClientModelForAdmin;
import com.malyszaryczlowiek.shop.client.ClientModelForAdminAssembler;
import com.malyszaryczlowiek.shop.client.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Dostęp tylko dla ADMINA.
 */
@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private final ClientRepository clientRepository;


    @Autowired
    AdminUserController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    /**
     * metoda która dostarcza Adminowi informacji o użytkowniku
     *
     * @param id - id użytkownika
     * @return obiekt użytkowanika
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ClientModelForAdmin> getFullUserInfo(@PathVariable(name = "id") Long id) {
        if (clientRepository.existsById(id)) {
            ClientModelForAdminAssembler model = new ClientModelForAdminAssembler();
            return ResponseEntity.status(HttpStatus.OK).body(model.toModel(clientRepository.getOne(id)));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    /**
     * metoda musi być public bo inaczej nie można się do niej odwołać jak chcemy
     * jej użyć w HATEOAS. TZN jak chcemy ją zlinkować
     * @return zwraca wszyskich użytkowników servisu
     */
    @RequestMapping(path = "/allClients",method = RequestMethod.GET)
    public ResponseEntity<Page<ClientModelForAdmin>> getAllClients(
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "sort", defaultValue = "desc") String sorting,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy) {

        Pageable pageable;
        Page<Client> pageOfClients;

        if (sorting.equals("asc") && sortBy.equals("time"))
            pageable = PageRequest.of(page, size, Sort.Direction.ASC, "email");
        else
            pageable = PageRequest.of(page, size, Sort.Direction.DESC, "email");
        pageOfClients = clientRepository.findClientsWithRole("ROLE_CLIENT" ,pageable);
        ClientModelForAdminAssembler modelAssembler = new ClientModelForAdminAssembler();
        Page<ClientModelForAdmin> pageOfClientsModel = pageOfClients.map(modelAssembler::toModel);
        return ResponseEntity.status(HttpStatus.OK)
                .body(pageOfClientsModel);
    }



    /**
     * metoda musi być public bo inaczej nie można się do niej odwołać jak chcemy
     * jej użyć w HATEOAS. TZN jak chcemy ją zlinkować
     * @return zwraca wszyskich użytkowników servisu
     */
    @RequestMapping(path = "/allUsers",method = RequestMethod.GET)
    public ResponseEntity<Page<ClientModelForAdmin>> getAllUsers(
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "sort", defaultValue = "desc") String sorting,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy) {

        Pageable pageable;
        if (sorting.equals("asc") && sortBy.equals("time"))
            pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        else
            pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        Page<Client> pageOfClients = clientRepository.findAll(pageable);
        ClientModelForAdminAssembler modelAssembler = new ClientModelForAdminAssembler();
        // strona z klient modelami otrzymanymi przez wywołanie na modelAssemblerze
        Page<ClientModelForAdmin> pageOfClientsModel = pageOfClients.map(modelAssembler::toModel);
        return ResponseEntity.status(HttpStatus.OK)
                .body(pageOfClientsModel);
    }


}


















