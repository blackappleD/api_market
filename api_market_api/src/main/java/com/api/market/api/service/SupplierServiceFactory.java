package com.api.market.api.service;

import cn.hutool.extra.spring.SpringUtil;
import com.api.market.api.dto.ApiBaseReqDTO;
import com.api.market.api.dto.ApiBaseResDTO;
import com.api.market.core.exception.MerchantException;
import com.api.market.core.exception.RateLimitException;
import com.api.market.core.exception.SupplierException;
import com.api.market.core.po.SupplierPO;
import com.api.market.core.service.MerchantService;
import com.api.market.core.service.SupplierApiService;
import com.api.market.core.util.RateLimitUtil;
import jakarta.annotation.Resource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
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

	@Resource
	private SupplierApiService supplierApiService;

	@Resource
	private MerchantService merchantService;

	private SupplierService getService(String apiCode) {

		List<SupplierPO> suppliers = supplierApiService.findAllByApiCode(apiCode);

		if (suppliers.isEmpty()) {
			throw SupplierException.notSupportBindThisApi();
		}

		// todo 根据路由规则获取供应商，暂时取第一个
		String supCode = suppliers.getFirst().getSupCode();
		SupplierService service = serviceMap.get(supCode);
		if (service == null) {
			throw new IllegalArgumentException("Invalid API code: " + supCode);
		}
		return service;
	}

	@SuppressWarnings("rawtypes")
	public ApiBaseResDTO execute(ApiBaseReqDTO params) {

		String merchantCode = params.getMerchantCode();
		String apiCode = params.getApiCode();

		boolean available = merchantService.isAccountAvailable(merchantCode, apiCode);

		// todo
		if (RateLimitUtil.isLimit()) {
			throw RateLimitException.rateLimit();
		}

		if (RateLimitUtil.isPerDayLimit()) {
			throw RateLimitException.perDataLimit();
		}

		if (!available) {
			throw MerchantException.merchantApiNotAvailable(merchantCode, apiCode);
		}

		return getService(apiCode).execute(params);
	}


	@Override
	public void run(ApplicationArguments args) {
		Map<String, SupplierService> beansOfType = SpringUtil.getBeansOfType(SupplierService.class);
		serviceMap.putAll(beansOfType);
	}

}
