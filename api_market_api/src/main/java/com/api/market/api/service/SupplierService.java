package com.api.market.api.service;

import com.api.market.api.dto.ApiBaseReqDTO;
import com.api.market.api.dto.ApiBaseResDTO;
import com.api.market.core.po.SupplierApiPO;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/10 12:55
 */
public interface SupplierService {

	@SuppressWarnings("rawtypes")
	ApiBaseResDTO execute(SupplierApiPO supApi, ApiBaseReqDTO dto);

}
