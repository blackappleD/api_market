package com.api.market.api.service;

import com.api.market.api.dto.ApiBaseReqDTO;
import com.api.market.api.dto.ApiBaseResDTO;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/10 12:55
 */
public interface SupplierService {

	@SuppressWarnings("rawtypes")
	ApiBaseResDTO execute(ApiBaseReqDTO params);

}
