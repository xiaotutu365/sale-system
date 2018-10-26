package com.trey.order.server.controller;

import com.trey.order.server.converter.OrderForm2OrderDtoConverter;
import com.trey.order.server.dto.OrderDto;
import com.trey.order.server.enums.ResultEnum;
import com.trey.order.server.exception.OrderException;
import com.trey.order.server.form.OrderForm;
import com.trey.order.server.service.OrderService;
import com.trey.order.server.utils.ResultVoUtils;
import com.trey.order.server.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResultVo<Map<String, String>> create(@Valid OrderForm orderForm, BindingResult bindingResult) throws OrderException {
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】参数不正确, orderForm={}", orderForm);
            throw new OrderException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
        // orderForm -> orderDto
        OrderDto orderDto = OrderForm2OrderDtoConverter.convert(orderForm);
        if (CollectionUtils.isEmpty(orderDto.getOrderDetailList())) {
            log.error("【创建订单】购物车信息为空");
            throw new OrderException(ResultEnum.CART_EMPTY);
        }
        OrderDto result = orderService.create(orderDto);
        Map<String, String> map = new HashMap<>();
        map.put("orderId", result.getOrderId());
        return ResultVoUtils.success(map);
    }
}