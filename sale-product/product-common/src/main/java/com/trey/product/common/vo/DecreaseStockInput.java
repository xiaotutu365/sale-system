package com.trey.product.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DecreaseStockInput {
    private String productId;

    private Integer productQuantity;
}