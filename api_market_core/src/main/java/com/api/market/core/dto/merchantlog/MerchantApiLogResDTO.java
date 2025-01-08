package com.api.market.core.dto.merchantlog;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MerchantApiLogResDTO {
	private String id;
	private Long merchantId;
	private String merchantName;
	private String apiId;
	private String apiName;
	private LocalDateTime requestTime;
	private LocalDateTime responseTime;
	private Integer duration;
	private String requestParams;
	private String responseData;
	private Integer status;
	private String errorMsg;
	private LocalDateTime createTime;
}