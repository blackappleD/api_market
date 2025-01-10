package com.api.market.mg.controller;

import com.api.market.core.annotations.PkAuthControl;
import com.api.market.core.annotations.PkResponseBody;
import com.api.market.core.dto.base.PageDTO;
import com.api.market.core.dto.category.ApiCategoryCreateReqDTO;
import com.api.market.core.dto.category.ApiCategoryQueryReqDTO;
import com.api.market.core.dto.category.ApiCategoryResDTO;
import com.api.market.core.dto.category.ApiCategoryUpdateReqDTO;
import com.api.market.core.service.ApiCategoryService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@PkResponseBody
@PkAuthControl
public class ApiCategoryController {

	@Resource
	private ApiCategoryService apiCategoryService;

	@PostMapping
	public Long create(@Valid @RequestBody ApiCategoryCreateReqDTO dto) {
		return apiCategoryService.create(dto);
	}

	@PutMapping
	public void update(@Valid @RequestBody ApiCategoryUpdateReqDTO dto) {
		apiCategoryService.update(dto);
	}

	@GetMapping("/{id}")
	public ApiCategoryResDTO get(@PathVariable Long id) {
		return apiCategoryService.get(id);
	}

	@PostMapping("/page")
	public PageDTO<ApiCategoryResDTO> page(@Valid @RequestBody ApiCategoryQueryReqDTO dto) {
		return apiCategoryService.page(dto);
	}

	@GetMapping("/list")
	public List<ApiCategoryResDTO> list() {
		return apiCategoryService.listAll();
	}
}