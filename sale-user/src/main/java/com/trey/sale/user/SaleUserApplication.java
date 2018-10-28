package com.trey.sale.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SaleUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaleUserApplication.class, args);
    }
}
