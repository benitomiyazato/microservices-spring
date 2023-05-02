package com.benitomiyazato.inventoryservice.repository;

import com.benitomiyazato.inventoryservice.model.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends MongoRepository<Inventory, String> {

    List<Inventory> findByNameIn(List<String> names);
}
