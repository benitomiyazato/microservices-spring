package com.benitomiyazato.inventoryservice.service;

import com.benitomiyazato.inventoryservice.dto.InventoryResponse;
import com.benitomiyazato.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public List<InventoryResponse> isInStock(List<String> names) {
        return inventoryRepository.findByNameIn(names)
                .stream()
                .map(inv ->
                        InventoryResponse.builder().name(inv.getName()).quantity(inv.getQuantity()).build())
                .toList();
    }
}
