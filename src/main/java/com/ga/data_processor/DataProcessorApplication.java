package com.ga.data_processor;

import com.ga.data_processor.service.SalaryProcesser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DataProcessorApplication implements CommandLineRunner {

	private final SalaryProcesser processer;

	public DataProcessorApplication(SalaryProcesser processer) {
		this.processer = processer;
	}

	public static void main(String[] args) {
		SpringApplication.run(DataProcessorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		processer.process();
	}
}
