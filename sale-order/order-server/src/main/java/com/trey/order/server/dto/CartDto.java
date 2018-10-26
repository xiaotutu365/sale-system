package com.trey.order.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CartDto {

    private String productId;


    private Integer productQuantity;
}
