package com.api.market.mg.controller;

import com.api.market.core.dto.merchant.*;
import com.api.market.core.service.MerchantService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Resource
    private MerchantService merchantService;

    @PostMapping
    public MerchantResDTO create(@Valid @RequestBody MerchantCreateReqDTO dto) {
        return merchantService.create(dto);
    }

    @PutMapping
    public MerchantResDTO update(@Valid @RequestBody MerchantUpdateReqDTO dto) {
        return merchantService.update(dto);
    }

    @GetMapping("/page")
    public Page<MerchantResDTO> page(@Valid MerchantQueryReqDTO dto) {
        return merchantService.query(dto);
    }
}