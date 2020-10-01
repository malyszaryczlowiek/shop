package com.malyszaryczlowiek.shop.order;

import com.malyszaryczlowiek.shop.client.Client;
import com.malyszaryczlowiek.shop.client.ClientRepository;
import com.malyszaryczlowiek.shop.controllerUtil.ControllerUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PositiveOrZero;



/**
 * Do kontrolera ma dostęp tylko USER.
 *
 * Nie ma możliwości usówania zamówień ani ich modyfikacji,
 * są to elementy już zakończone i można oglądać tylko ich
 * zawartość.
 */
@RestController
@RequestMapping("/myAccount/myOrders")
public class MyOrdersController {

    private final Logger logger = LoggerFactory.getLogger(MyOrdersController.class);

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ControllerUtil controllerUtil;

    @Autowired
    public MyOrdersController(OrderRepository orderRepository,
                              ClientRepository clientRepository,
                              ControllerUtil controllerUtil) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.controllerUtil = controllerUtil;
    }


    /**
     * Jedyna metoda w tym kontrolerze. dostarcza stronę z wykonanymi zamówieniami
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<OrderModel>> getMyOrders(
            @RequestParam(name = "page", defaultValue = "0", required = false) @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) @PositiveOrZero int size,
            @RequestParam(name = "sort", defaultValue = "d", required = false) String sorting,
            @RequestParam(name = "sortBy", defaultValue = "orderDate", required = false) String sortBy,
            @RequestParam(name = "after", required = false) Long after,
            @RequestParam(name = "before", required = false) Long before,
            Authentication authentication) {

        Pageable pageable = controllerUtil.setPaging(page, size, sorting, sortBy);
        String email = authentication.getName();
        Client client = clientRepository.findByEmail(email);
        if (client != null) {
            Page<Order> orders;
            if (after != null && before != null)
                orders = orderRepository.findOrdersByClientBetween(client, after, before, pageable);
            else if (after == null && before != null)
                orders = orderRepository.findOrdersByClientBefore(client, before, pageable);
            else if (after != null)
                orders = orderRepository.findOrdersByClientAfter(client, after, pageable);
            else
                orders = orderRepository.findOrdersByClient(client, pageable);
            if (orders.getSize() >0 )
                logger.debug("Page<Order> ma standardowo rozmiar 10 (są to zamówienia użytkownika): " + orders.getSize());
            else {
                logger.debug("nie ma żadnego zrealizowanego zamówienia. Page<Order> jest pusty");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(orders.map(OrderModel::new));
        }
        authentication.setAuthenticated(false);
        logger.debug("Nie udało się uzyskać uwierzytelnienia clienta.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}











/*
@RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getConcreteOrder(@PathVariable(name = "id") Long id) {

        return "concrete order: " + id;
    }
 */