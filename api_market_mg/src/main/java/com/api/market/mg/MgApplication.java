package com.api.market.mg;

import com.api.market.core.CoreSource;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MgApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder().sources(
				MgApplication.class,
				CoreSource.class
		).run(args);
	}
}