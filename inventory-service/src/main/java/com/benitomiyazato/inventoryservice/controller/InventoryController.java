package com.benitomiyazato.inventoryservice.controller;

import com.benitomiyazato.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventories")
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/{name}")
    public boolean isInStock(@PathVariable(name = "name") String name) {

        return inventoryService.isInStock(name);
    }
}
