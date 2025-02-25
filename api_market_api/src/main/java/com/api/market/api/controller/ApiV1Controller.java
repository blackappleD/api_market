package com.api.market.api.controller;

import com.api.market.api.dto.ApiBaseReqDTO;
import com.api.market.api.dto.ApiBaseResDTO;
import com.api.market.api.service.SupplierServiceFactory;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/10 12:54
 */
@RestController
@RequestMapping("/v1")
public class ApiV1Controller {

	@Resource
	private SupplierServiceFactory supplierServiceFactory;

	@SuppressWarnings("rawtypes")
	@PostMapping
	public ApiBaseResDTO get(@RequestBody @Valid ApiBaseReqDTO reqDTO) {
		return supplierServiceFactory.execute(reqDTO);
	}

}
