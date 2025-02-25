package com.api.market.api.service;

import com.api.market.api.dto.ApiBaseReqDTO;
import com.api.market.api.dto.ApiBaseResDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/13 9:49
 */
@Component
public class ApiRequestHandler {

	@Resource
	private SupplierServiceFactory supplierServiceFactory;

	public ApiBaseResDTO request(String apiCode, ApiBaseReqDTO req) {

		return supplierServiceFactory.getService(apiCode).execute(apiCode, req);

	}

}
