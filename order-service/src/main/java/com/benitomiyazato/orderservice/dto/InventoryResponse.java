package com.benitomiyazato.orderservice.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private String name;
    private Integer quantity;

    public boolean isInStock() {
        return quantity > 0;
    }
}
