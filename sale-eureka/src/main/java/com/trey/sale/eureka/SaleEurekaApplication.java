package com.trey.sale.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class SaleEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaleEurekaApplication.class, args);
    }
}
