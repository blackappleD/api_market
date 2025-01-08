package com.api.market.mg.controller;

import com.api.market.core.dto.category.*;
import com.api.market.core.service.ApiCategoryService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class ApiCategoryController {

    @Resource
    private ApiCategoryService apiCategoryService;

    @PostMapping
    public ApiCategoryResDTO create(@Valid @RequestBody ApiCategoryCreateReqDTO dto) {
        return apiCategoryService.create(dto);
    }

    @PutMapping
    public ApiCategoryResDTO update(@Valid @RequestBody ApiCategoryUpdateReqDTO dto) {
        return apiCategoryService.update(dto);
    }

    @GetMapping("/page")
    public Page<ApiCategoryResDTO> page(@Valid ApiCategoryQueryReqDTO dto) {
        return apiCategoryService.query(dto);
    }

    @GetMapping("/list")
    public List<ApiCategoryResDTO> list() {
        return apiCategoryService.listAll();
    }
}