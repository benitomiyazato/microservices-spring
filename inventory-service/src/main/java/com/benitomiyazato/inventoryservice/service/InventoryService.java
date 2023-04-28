package com.benitomiyazato.inventoryservice.service;

import com.benitomiyazato.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String name) {
        return inventoryRepository.findByName(name).isPresent();
    }
}
