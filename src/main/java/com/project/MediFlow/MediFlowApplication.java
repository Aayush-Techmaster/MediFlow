package com.project.MediFlow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRetry
@EnableScheduling
@SpringBootApplication
public class MediFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediFlowApplication.class, args);
	}

}
