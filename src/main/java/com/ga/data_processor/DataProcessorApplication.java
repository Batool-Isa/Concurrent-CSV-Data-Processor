package com.ga.data_processor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class DataProcessorApplication  {

	public static void main(String[] args) {
		SpringApplication.run(DataProcessorApplication.class, args);
	}
	@Bean
	CommandLineRunner run(SalaryProcesser processor) {
		return args -> processor.process();
	}

}
