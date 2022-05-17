package com.dhruvi.umsboot;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UmsbootApplication {

	public static void main(String[] args) {
		BasicConfigurator.configure();
		SpringApplication.run(UmsbootApplication.class, args);
	}
}
