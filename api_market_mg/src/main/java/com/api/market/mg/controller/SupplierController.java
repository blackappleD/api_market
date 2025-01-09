package com.api.market.mg.controller;

import com.api.market.core.annotations.PkResponseBody;
import com.api.market.core.dto.BatchEnableLongIdReqDTO;
import com.api.market.core.dto.base.PageDTO;
import com.api.market.core.dto.supplier.SupplierCreateReqDTO;
import com.api.market.core.dto.supplier.SupplierQueryReqDTO;
import com.api.market.core.dto.supplier.SupplierResDTO;
import com.api.market.core.dto.supplier.SupplierUpdateReqDTO;
import com.api.market.core.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "供应商管理", description = "供应商相关接口")
@RestController
@RequestMapping("/supplier")
@PkResponseBody
public class SupplierController {

	@Resource
	private SupplierService supplierService;

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

	@Operation(summary = "导出供应商数据")
	@GetMapping("/export")
	public void export(SupplierQueryReqDTO req, HttpServletResponse response) throws IOException {
		supplierService.export(req, response);
	}

	@Operation(summary = "导入供应商数据")
	@PostMapping("/import")
	public void importData(@RequestParam("file") MultipartFile file) throws IOException {
		supplierService.importData(file);
	}
}