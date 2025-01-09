package com.api.market.core.dto.api;

import com.api.market.core.dto.base.BasePageReqDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiQueryReqDTO extends BasePageReqDTO {
	private Long categoryId;
	private Boolean enable;
}