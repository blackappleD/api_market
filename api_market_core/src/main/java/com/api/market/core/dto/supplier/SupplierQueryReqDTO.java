package com.api.market.core.dto.supplier;

import com.api.market.core.dto.base.BasePageReqDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SupplierQueryReqDTO extends BasePageReqDTO {
    private String supplierName;
    private String supplierCode;
    private String contactName;
    private Integer status;
}