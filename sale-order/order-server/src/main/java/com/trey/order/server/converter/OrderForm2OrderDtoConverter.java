package com.trey.order.server.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trey.order.server.dto.OrderDto;
import com.trey.order.server.entity.OrderDetail;
import com.trey.order.server.enums.ResultEnum;
import com.trey.order.server.exception.OrderException;
import com.trey.order.server.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class OrderForm2OrderDtoConverter {
    public static OrderDto convert(OrderForm orderForm) {
        Gson gson = new Gson();
        OrderDto orderDto = new OrderDto();
        orderDto.setBuyerName(orderForm.getName());
        orderDto.setBuyerPhone(orderForm.getPhone());
        orderDto.setBuyerAddress(orderForm.getAddress());
        orderDto.setBuyerOpenid(orderForm.getOpenid());

        List<OrderDetail> orderDetailList;
        try {
            orderDetailList = gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>() {
            }.getType());
        } catch (Exception e) {
            log.error("【json转换】错误, string={}", orderForm.getItems());
            throw new OrderException(ResultEnum.PARAM_ERROR);
        }
        orderDto.setOrderDetailList(orderDetailList);
        return orderDto;
    }
}