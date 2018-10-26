package com.trey.order.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.trey.product.client")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.trey.product.client", "com.trey.order.server"})
public class SaleOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaleOrderApplication.class, args);
    }
}