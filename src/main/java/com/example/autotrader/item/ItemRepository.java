package com.example.autotrader.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT c FROM Item c WHERE c.name = ?1")
    Item findByName(String itemName);
}
