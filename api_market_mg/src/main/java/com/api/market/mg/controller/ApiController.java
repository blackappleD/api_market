package com.api.market.mg.controller;

import com.api.market.core.annotations.PkAuthControl;
import com.api.market.core.annotations.PkResponseBody;
import com.api.market.core.dto.BatchEnableLongIdReqDTO;
import com.api.market.core.dto.api.ApiCreateReqDTO;
import com.api.market.core.dto.api.ApiQueryReqDTO;
import com.api.market.core.dto.api.ApiResDTO;
import com.api.market.core.dto.api.ApiUpdateReqDTO;
import com.api.market.core.dto.base.PageDTO;
import com.api.market.core.service.ApiService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@PkResponseBody
@PkAuthControl
public class ApiController {

	@Resource
	private ApiService apiService;

	@PostMapping
	public Long create(@Valid @RequestBody ApiCreateReqDTO dto) {
		return apiService.create(dto);
	}

	@PutMapping
	public void update(@Valid @RequestBody ApiUpdateReqDTO dto) {
		apiService.update(dto);
	}

	@GetMapping("/{id}")
	public ApiResDTO get(@PathVariable Long id) {
		return apiService.get(id);
	}

	@PostMapping("/page")
	public PageDTO<ApiResDTO> page(@Valid @RequestBody ApiQueryReqDTO dto) {
		return apiService.search(dto);
	}

	@PutMapping("/enable")
	public void batchEnable(@Valid @RequestBody BatchEnableLongIdReqDTO dto) {
		apiService.batchUpdateStatus(dto);
	}

}