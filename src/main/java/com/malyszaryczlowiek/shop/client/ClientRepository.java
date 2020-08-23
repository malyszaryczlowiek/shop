package com.malyszaryczlowiek.shop.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * @param email klienta, którego obiekt chcemy wyłuskać
     * @return obiekt klienta lub null jeśli nie ma klienta w bazie danych.
     */
    @Query("SELECT c FROM Client c WHERE c.email=:email")
    Client findByEmail(@Param("email") String email);



    /**
     * @param email email użytkownika (może istnieć w bazie tylko jeden albo żaden)
     *              patrz {@link Client}.
     * @return liczna użytkowników w bazie o danym adresie email
     *         (zawsze będzie 0 lub 1)
     */
    @Query("SELECT count(c) FROM Client c WHERE c.email=:email")
    long checkNumberOfClientWithThisEmail(@Param("email") String email);


    /**
     * query używane przez Adminów.
     * @param role
     * @param pageable
     * @return
     */
    @Query("SELECT c FROM Client c WHERE c.roles LIKE :role")
    Page<Client> findClientsWithRole(@Param("role") String role, Pageable pageable);




    @Query("SELECT c.pass FROM Client c WHERE c.email=:email")
    String getPasswordFromEmail(@Param("email") String email);


}
