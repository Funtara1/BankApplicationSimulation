package com.Fuad.BankApplicationSimulation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BankApplicationSimulationApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplicationSimulationApplication.class, args);
	}
}
