package com.ga.data_processor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataProcessorApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DataProcessorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		DataReader.readData();
	}
}
