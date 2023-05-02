package com.benitomiyazato.inventoryservice.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private String name;
    private Integer quantity;
}
