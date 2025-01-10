package com.api.market.mg.controller;

import com.api.market.core.annotations.PkAuthControl;
import com.api.market.core.annotations.PkResponseBody;
import com.api.market.core.dto.BatchEnableLongIdReqDTO;
import com.api.market.core.dto.base.PageDTO;
import com.api.market.core.dto.supplier.*;
import com.api.market.core.service.SupplierApiService;
import com.api.market.core.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "供应商管理", description = "供应商相关接口")
@RestController
@RequestMapping("/supplier")
@PkResponseBody
@PkAuthControl
public class SupplierController {

	@Resource
	private SupplierService supplierService;

	@Resource
	private SupplierApiService supplierApiService;

	@Operation(summary = "创建供应商")
	@PostMapping
	public Long create(@Valid @RequestBody SupplierCreateReqDTO req) {
		return supplierService.create(req);
	}

	@Operation(summary = "更新供应商")
	@PutMapping
	public void update(@Valid @RequestBody SupplierUpdateReqDTO req) {
		supplierService.update(req);
	}

	@Operation(summary = "根据ID获取供应商")
	@GetMapping("/{id}")
	public SupplierResDTO getById(@PathVariable Long id) {
		return supplierService.get(id);
	}

	@Operation(summary = "分页查询供应商列表")
	@PostMapping("/page")
	public PageDTO<SupplierResDTO> page(@Valid @RequestBody SupplierQueryReqDTO req) {
		return supplierService.search(req);
	}

	@Operation(summary = "批量启用/通用供应商")
	@PostMapping("/enable")
	public void batchEnable(@RequestBody BatchEnableLongIdReqDTO dto) {
		supplierService.batchUpdateStatus(dto);
	}

	@Operation(summary = "供应商绑定Api")
	@PostMapping("/api/bind")
	public Long createApiBind(@RequestBody SupplierApiCreateDTO dto) {
		return supplierApiService.create(dto);
	}

	@Operation(summary = "更新供应商绑定Api信息")
	@PutMapping("/api/bind")
	public void updateApiBind(@RequestBody SupplierApiUpdateDTO dto) {
		supplierApiService.update(dto);
	}

	@Operation(summary = "供应商Api列表查询")
	@GetMapping("/api/page")
	public PageDTO<SupplierApiResDTO> searchApi(@Valid @RequestBody SupplierApiQueryReqDTO req) {
		return supplierApiService.search(req);
	}

	

}