package com.api.market.mg.controller;

import com.api.market.core.dto.api.*;
import com.api.market.core.service.ApiService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Resource
    private ApiService apiService;

    @PostMapping
    public ApiResDTO create(@Valid @RequestBody ApiCreateReqDTO dto) {
        return apiService.create(dto);
    }

    @PutMapping
    public ApiResDTO update(@Valid @RequestBody ApiUpdateReqDTO dto) {
        return apiService.update(dto);
    }

    @GetMapping("/page")
    public Page<ApiResDTO> page(@Valid ApiQueryReqDTO dto) {
        return apiService.query(dto);
    }
}