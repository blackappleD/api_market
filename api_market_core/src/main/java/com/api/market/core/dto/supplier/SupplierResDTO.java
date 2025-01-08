package com.api.market.core.dto.supplier;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SupplierResDTO {
    private String id;
    private String supplierName;
    private String supplierCode;
    private String description;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private String address;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}