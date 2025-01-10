package com.api.market.core.api.service;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/10 13:48
 */
@Component
public class SupplierServiceFactory implements ApplicationRunner {

	private final Map<String, SupplierService> serviceMap = new ConcurrentHashMap<>();


	public SupplierService getService(String supCode) {
		SupplierService service = serviceMap.get(supCode);
		if (service == null) {
			throw new IllegalArgumentException("Invalid API code: " + supCode);
		}
		return service;
	}

	@Override
	public void run(ApplicationArguments args) {
		Map<String, SupplierService> beansOfType = SpringUtil.getBeansOfType(SupplierService.class);
		serviceMap.putAll(beansOfType);
	}

}
