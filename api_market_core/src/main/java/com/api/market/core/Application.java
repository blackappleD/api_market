package com.api.market.core;

import com.api.market.core.po.UserPO;
import com.api.market.core.repo.UserRepo;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackageClasses = UserPO.class)
@EnableJpaRepositories(basePackageClasses = UserRepo.class)
public class Application {

	public static void main(String[] args) {
		new SpringApplicationBuilder().sources(
				Application.class
		).run(args);
	}
}