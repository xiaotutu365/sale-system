package com.trey.product.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SaleProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(SaleProductApplication.class, args);
    }
}