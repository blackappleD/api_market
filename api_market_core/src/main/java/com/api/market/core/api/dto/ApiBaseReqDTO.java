package com.api.market.core.api.dto;

import lombok.Data;

@Data
public abstract class ApiBaseReqDTO {

	private String appKey;

	private String timestamp;

	private String reqNum;

	private String sign;


}
