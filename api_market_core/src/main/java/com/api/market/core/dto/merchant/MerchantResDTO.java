package com.api.market.core.dto.merchant;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MerchantResDTO {
    private String id;
    private String merchantName;
    private String merchantCode;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private String appKey;
    private String appSecret;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}