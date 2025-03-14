package com.api.market.api.service;

import cn.hutool.extra.spring.SpringUtil;
import com.api.market.api.dto.ApiBaseReqDTO;
import com.api.market.api.dto.ApiBaseResDTO;
import com.api.market.core.enums.ApiCode;
import com.api.market.core.exception.ApiException;
import com.api.market.core.exception.MerchantException;
import com.api.market.core.exception.RateLimitException;
import com.api.market.core.exception.SupplierException;
import com.api.market.core.po.ApiPO;
import com.api.market.core.po.ApiSalePO;
import com.api.market.core.po.MerchantPO;
import com.api.market.core.po.SupplierApiPO;
import com.api.market.core.service.ApiSaleService;
import com.api.market.core.service.ApiService;
import com.api.market.core.service.MerchantService;
import com.api.market.core.service.SupplierApiService;
import com.api.market.core.util.RateLimitUtil;
import jakarta.annotation.Resource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
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

	@Resource
	private ApiSaleService apiSaleService;

	@Resource
	private ApiService apiService;

	private SupplierService getService(SupplierApiPO supplierApi) {

		SupplierService service = serviceMap.get(supplierApi.getSupplier().getSupCode());
		if (service == null) {
			throw SupplierException.supplierServiceNotSettle();
		}
		return service;
	}

	@SuppressWarnings("rawtypes")
	public ApiBaseResDTO execute(ApiBaseReqDTO reqDto) {

		String merchantCode = reqDto.getMerchantCode();
		ApiCode apiCode = reqDto.getApiCode();

		MerchantPO merchant = merchantService.findByMerchantCode(merchantCode);
		if (!merchant.getEnable()) {
			throw MerchantException.merchantNotAvailable();
		}
		ApiPO api = apiService.findByApiCode(apiCode);

		if (!api.getEnable()) {
			throw ApiException.apiDisabled();
		}
		ApiSalePO apiSale = apiSaleService.findBYMerchantAndApi(merchant, api);

		if (!apiSale.getEnable()) {
			throw MerchantException.merchantApiNotAvailable(merchant.getMerCode(), api.getApiCode());
		}

		// todo 限流
		if (RateLimitUtil.isLimit()) {
			throw RateLimitException.rateLimit();
		}

		if (RateLimitUtil.isPerDayLimit()) {
			throw RateLimitException.perDataLimit();
		}

		Optional<SupplierApiPO> first = apiSale.getRouterSupplierApis()
				.stream()
				.min(Comparator.comparing(SupplierApiPO::getPriority));
		if (first.isEmpty()) {
			throw MerchantException.merchantApiNotAvailable(merchantCode, apiCode);
		}

		return getService(first.get()).execute(first.get(), reqDto);
	}


	@Override
	public void run(ApplicationArguments args) {
		Map<String, SupplierService> beansOfType = SpringUtil.getBeansOfType(SupplierService.class);
		serviceMap.putAll(beansOfType);
	}

}
