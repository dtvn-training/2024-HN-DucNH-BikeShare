package com.ducnh.bikeshare;

import com.google.cloud.bigquery.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BikeshareApplication {

	public static void main(String[] args) {
		SpringApplication.run(BikeshareApplication.class, args);
	}
}