package com.trey.product.server.enums;

import lombok.AllArgsConstructor;

/**
 * 商品上下架状态
 */
@AllArgsConstructor
public enum ProductStatusEnum {

    UP(0, "在架"),

    DOWN(1, "下架");

    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}