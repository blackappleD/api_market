package com.api.market.mg.controller;

import com.api.market.core.dto.merchantlog.MerchantApiLogQueryReqDTO;
import com.api.market.core.dto.merchantlog.MerchantApiLogResDTO;
import com.api.market.core.service.MerchantApiLogService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchant/log")
public class MerchantApiLogController {

    @Resource
    private MerchantApiLogService merchantApiLogService;

    @GetMapping("/page")
    public Page<MerchantApiLogResDTO> page(@Valid MerchantApiLogQueryReqDTO dto) {
        return merchantApiLogService.query(dto);
    }
}