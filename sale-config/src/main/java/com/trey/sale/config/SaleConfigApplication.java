package com.trey.sale.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class SaleConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaleConfigApplication.class, args);
    }
}
