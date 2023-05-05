package com.benitomiyazato.notificationservice.repository;

import com.benitomiyazato.notificationservice.model.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends MongoRepository<Inventory, String> {

    List<Inventory> findByNameIn(List<String> names);
}
