package com.trey.order.server.message;

import com.alibaba.fastjson.JSON;
import com.trey.product.common.vo.ProductInfoOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProductInfoReceiver {

    private static final String PRODUCT_STOCK_TEMPLATE = "product_stock_%s";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RabbitListener(queuesToDeclare = @Queue("productInfo"))
    public void process(String message) {
        // message => ProductInfoOutput
        List<ProductInfoOutput> productInfoOutputList = JSON.parseArray(message, ProductInfoOutput.class);
        log.info("从队列【{}】中接收到消息：{}.", "productInfo", productInfoOutputList);

        // 接收到消息后，将消息存储在redis中
        productInfoOutputList.forEach(productInfoOutput -> {
            redisTemplate.opsForValue().set(String.format(PRODUCT_STOCK_TEMPLATE, productInfoOutput.getProductId()), String.valueOf(productInfoOutput.getProductStock()));
        });
    }
}