package com.pranavsalaria.Inventory.repository;

import com.pranavsalaria.Inventory.model.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    Optional<Item> findByItemNo(String itemNo);

}
