package com.api.market.core;

import com.api.market.core.po.UserPO;
import com.api.market.core.repo.UserRepo;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2023/5/25 10:18
 */
@SpringBootApplication
@EntityScan(basePackageClasses = UserPO.class)
@EnableJpaRepositories(basePackageClasses = UserRepo.class)
public class CoreSource {
}
