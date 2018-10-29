package com.trey.order.server.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
@RequestMapping("/hystrix")
public class HystrixController {

    /**
     * 可以全局配置，也可以单个方法配置
     * 可以在注解中配置，也可以在配置文件中配置
     * @return
     */
    @HystrixCommand
    @GetMapping("/getProductInfoList")
    public String getProductInfoList() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject("http://localhost:8088/product/listForOrder",
                Arrays.asList("1"),
                String.class);
    }
}