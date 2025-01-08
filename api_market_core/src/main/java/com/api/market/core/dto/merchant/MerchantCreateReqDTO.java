package com.api.market.core.dto.merchant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MerchantCreateReqDTO {
    @NotBlank(message = "商户名称不能为空")
    private String merchantName;

    @NotBlank(message = "商户编码不能为空")
    private String merchantCode;

    private String contactName;

    private String contactPhone;

    @Email(message = "邮箱格式不正确")
    private String contactEmail;
}