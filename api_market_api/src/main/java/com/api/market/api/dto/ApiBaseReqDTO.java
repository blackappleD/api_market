package com.api.market.api.dto;

import com.api.market.core.enums.ApiCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApiBaseReqDTO {

	@NotNull(message = "apiCode不能为空")
	private ApiCode apiCode;

	@NotBlank(message = "merchantCode不能为空")
	private String merchantCode;

	@NotBlank(message = "appKey不能为空")
	private String appKey;

	@NotBlank(message = "timestamp不能为空")
	private String timestamp;

	@NotBlank(message = "seqNo不能为空")
	private String seqNo;

	@NotBlank(message = "sign不能为空")
	private String sign;

	@NotNull(message = "业务请求参数不能为空")
	private Object params;

}
