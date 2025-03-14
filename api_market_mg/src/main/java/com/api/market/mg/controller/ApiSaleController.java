package com.api.market.mg.controller;

import com.api.market.core.annotations.PkAuthControl;
import com.api.market.core.annotations.PkResponseBody;
import com.api.market.core.dto.BatchEnableLongIdReqDTO;
import com.api.market.core.dto.apisale.ApiSaleCreateReqDTO;
import com.api.market.core.dto.apisale.ApiSaleQueryReqDTO;
import com.api.market.core.dto.apisale.ApiSaleResDTO;
import com.api.market.core.dto.apisale.ApiSaleUpdateReqDTO;
import com.api.market.core.dto.base.PageDTO;
import com.api.market.core.service.ApiSaleService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale")
@PkResponseBody
@PkAuthControl
public class ApiSaleController {

	@Resource
	private ApiSaleService apiSaleService;

	@PostMapping
	public Long create(@Valid @RequestBody ApiSaleCreateReqDTO dto) {
		return apiSaleService.create(dto);
	}

	@PutMapping
	public void update(@Valid @RequestBody ApiSaleUpdateReqDTO dto) {
		apiSaleService.update(dto);
	}

	@GetMapping("/{id}")
	public ApiSaleResDTO get(@PathVariable Long id) {
		return apiSaleService.get(id);
	}

	@GetMapping("/list")
	public List<ApiSaleResDTO> list() {
		return apiSaleService.list();
	}

	@PostMapping("/page")
	public PageDTO<ApiSaleResDTO> page(@Valid @RequestBody ApiSaleQueryReqDTO dto) {
		return apiSaleService.search(dto);
	}

	@PutMapping("/enable")
	public void batchEnable(@Valid @RequestBody BatchEnableLongIdReqDTO dto) {
		apiSaleService.batchUpdateStatus(dto);
	}
}