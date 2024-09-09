package com.ecommerce.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.ecommerce.test")
public class EcommerceBackendTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceBackendTestApplication.class, args);
	}

}