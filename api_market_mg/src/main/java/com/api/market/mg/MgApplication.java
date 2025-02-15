package com.api.market.mg;

import com.api.market.core.CoreSource;
import com.api.market.core.po.UserPO;
import com.api.market.core.repo.UserRepo;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackageClasses = UserPO.class)
@EnableJpaRepositories(basePackageClasses = UserRepo.class)
@EnableJpaAuditing
@OpenAPIDefinition(info = @Info(title = "API", version = "1.0"))
public class MgApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder().sources(
				MgApplication.class,
				CoreSource.class
		).run(args);
	}
}