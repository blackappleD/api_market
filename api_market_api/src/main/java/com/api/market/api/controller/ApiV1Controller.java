package com.api.market.api.controller;

import com.api.market.core.api.dto.ApiBaseReqDTO;
import com.api.market.core.api.dto.ApiBaseResDTO;
import com.api.market.core.api.service.SupplierService;
import jakarta.annotation.Resource;
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
	private SupplierService supplierService;

	@PostMapping("/{apiCode}")
	public <T> ApiBaseResDTO<T> get(@PathVariable String apiCode,
	                                @RequestBody ApiBaseReqDTO reqDTO) {


	}

}
