package com.benitomiyazato.notificationservice.controller;

import com.benitomiyazato.notificationservice.dto.InventoryResponse;
import com.benitomiyazato.notificationservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventories")
public class InventoryController {
    private final InventoryService inventoryService;

    // http://localhost:8082/api/inventories?name=iphone13&name=samsung22
    @GetMapping("")
    public List<InventoryResponse> isInStock(@RequestParam(name = "name") List<String> names) {
        return inventoryService.isInStock(names);
    }
}
