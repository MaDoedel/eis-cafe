package com.example.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.shop.model.Cup;
import com.example.shop.model.CupInfoPojo;

public interface CupRepository extends JpaRepository<Cup, Long> {

    @Query("SELECT c FROM Cup c WHERE c.name = ?1")
    List<Cup> findByName(String name);

    @Query("SELECT new com.example.shop.model.CupInfoPojo(c.id, f.id, COUNT(f.id)) FROM Cup c JOIN c.flavours f GROUP BY c.id, f.id")
    List<CupInfoPojo> findCupFlavours();

    @Query("SELECT new com.example.shop.model.CupInfoPojo(c.id, t.id, COUNT(t.id)) FROM Cup c JOIN c.toppings t GROUP BY c.id, t.id")
    List<CupInfoPojo> findCupToppings();
}