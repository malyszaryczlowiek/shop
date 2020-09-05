package com.malyszaryczlowiek.shop.order;

import com.malyszaryczlowiek.shop.client.Client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.client=:client")
    Page<Order> findOrdersByClient(@Param(value = "client") Client client, Pageable pageable);



    @Query("SELECT o FROM Order o WHERE o.client=:client AND o.orderDate<:before")
    Page<Order> findOrdersByClientBefore(
            @Param(value = "client") Client client,
            @Param(value = "before") Long before,
            Pageable pageable);



    @Query("SELECT o FROM Order o WHERE o.client=:client AND o.orderDate>:after")
    Page<Order> findOrdersByClientAfter(
            @Param(value = "client") Client client,
            @Param(value = "after") Long after,
            Pageable pageable);



    @Query("SELECT o FROM Order o WHERE o.client=:client AND o.orderDate>:after  AND o.orderDate<:before")
    Page<Order> findOrdersByClientBetween(
            @Param(value = "client") Client client,
            @Param(value = "after") Long after,
            @Param(value = "before") Long before,
            Pageable pageable);

}
