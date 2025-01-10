package com.api.market.mg.controller;

import com.api.market.core.annotations.PkAuthControl;
import com.api.market.core.annotations.PkResponseBody;
import com.api.market.core.dto.base.PageDTO;
import com.api.market.core.dto.merchantlog.MerchantApiLogQueryReqDTO;
import com.api.market.core.dto.merchantlog.MerchantApiLogResDTO;
import com.api.market.core.service.MerchantApiLogService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/merchant/log")
@PkResponseBody
@PkAuthControl
public class MerchantApiLogController {

	@Resource
	private MerchantApiLogService merchantApiLogService;

	@PostMapping("/page")
	public PageDTO<MerchantApiLogResDTO> page(@Valid @RequestBody MerchantApiLogQueryReqDTO dto) {
		return merchantApiLogService.page(dto);
	}

}