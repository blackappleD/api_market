package com.api.market.mg.controller;

import com.api.market.core.annotations.PkAuthControl;
import com.api.market.core.annotations.PkResponseBody;
import com.api.market.core.dto.base.PageDTO;
import com.api.market.core.dto.merchant.MerchantCreateReqDTO;
import com.api.market.core.dto.merchant.MerchantQueryReqDTO;
import com.api.market.core.dto.merchant.MerchantResDTO;
import com.api.market.core.dto.merchant.MerchantUpdateReqDTO;
import com.api.market.core.service.MerchantService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/merchant")
@PkResponseBody
@PkAuthControl
public class MerchantController {

	@Resource
	private MerchantService merchantService;

	@PostMapping
	public Long create(@Valid @RequestBody MerchantCreateReqDTO dto) {
		return merchantService.create(dto);
	}

	@PutMapping
	public void update(@Valid @RequestBody MerchantUpdateReqDTO dto) {
		merchantService.update(dto);
	}

	@PostMapping("/page")
	public PageDTO<MerchantResDTO> page(@Valid @RequestBody MerchantQueryReqDTO dto) {
		return merchantService.search(dto);
	}

	@GetMapping("/{id}")
	public MerchantResDTO get(@PathVariable Long id) {
		return merchantService.get(id);
	}

	@PostMapping("/{id}/send_email")
	public void sendAkSkEmail(@PathVariable Long id) {
		merchantService.sendAkSk(id);
	}

}