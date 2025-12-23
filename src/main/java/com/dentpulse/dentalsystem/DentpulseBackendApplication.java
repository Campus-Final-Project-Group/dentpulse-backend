package com.dentpulse.dentalsystem;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DentpulseBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DentpulseBackendApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();

	}

}
