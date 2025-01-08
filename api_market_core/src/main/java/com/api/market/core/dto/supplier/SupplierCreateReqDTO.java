package com.api.market.core.dto.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupplierCreateReqDTO {
    @NotBlank(message = "供应商名称不能为空")
    private String supplierName;

    @NotBlank(message = "供应商编码不能为空")
    private String supplierCode;

    private String description;

    private String contactName;

    private String contactPhone;

    @Email(message = "邮箱格式不正确")
    private String contactEmail;

    private String address;
}