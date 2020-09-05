package com.malyszaryczlowiek.shop.order;

import com.malyszaryczlowiek.shop.client.Client;
import com.malyszaryczlowiek.shop.client.ClientRepository;
import com.malyszaryczlowiek.shop.controllerUtil.ControllerUtil;

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
@RequestMapping("/myOrders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ControllerUtil controllerUtil;

    @Autowired
    public OrderController(OrderRepository orderRepository,
                           ClientRepository clientRepository,
                           ControllerUtil controllerUtil) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.controllerUtil = controllerUtil;
    }


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<OrderModel>> getMyOrders(
            @RequestParam(name = "page", defaultValue = "0", required = false) @PositiveOrZero int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) @PositiveOrZero int size,
            @RequestParam(name = "sort", defaultValue = "d", required = false) String sorting,
            @RequestParam(name = "sortBy", defaultValue = "orderDate", required = false) String sortBy,
            Authentication authentication) {

        Pageable pageable = controllerUtil.setPaging(page, size, sorting, sortBy);
        String email = authentication.getName();
        Client client = clientRepository.findByEmail(email);
        if (client != null) {
            Page<Order> orders = orderRepository.findOrdersByClient(client, pageable);
            OrderModelAssembler assembler = new OrderModelAssembler();
            return ResponseEntity.status(HttpStatus.OK).body(orders.map(assembler::toModel));
        }
        authentication.setAuthenticated(false);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }



    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getConcreteOrder(@PathVariable(name = "id") Long id) {



        return "concrete order: " + id;
    }





}
