package com.tauanoliveira.softwaretec;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SoftwaretecApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(SoftwaretecApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}