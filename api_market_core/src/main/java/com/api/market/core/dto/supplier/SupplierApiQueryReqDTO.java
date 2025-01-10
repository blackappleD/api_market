package com.api.market.core.dto.supplier;

import com.api.market.core.dto.base.BasePageReqDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SupplierApiQueryReqDTO extends BasePageReqDTO {
	private Long supplierId;
	private Long apiId;
	private Boolean enable;
}