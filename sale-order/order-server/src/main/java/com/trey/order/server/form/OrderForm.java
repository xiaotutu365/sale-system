package com.trey.order.server.form;

import lombok.Data;

/**
 * 订单表单
 */
@Data
public class OrderForm {
    private String name;

    private String phone;

    private String address;

    private String openid;

    private String items;
}