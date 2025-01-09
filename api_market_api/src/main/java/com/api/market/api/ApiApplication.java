package com.api.market.api;

import com.api.market.core.CoreSource;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApiApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder().sources(
				ApiApplication.class,
				CoreSource.class
		).run(args);
	}
}