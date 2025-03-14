package com.api.market.core.dto.supplier;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupplierCreateReqDTO {
	@NotBlank(message = "供应商名称不能为空")
	private String name;

	@NotBlank(message = "供应商编码不能为空")
	private String supCode;

	private String baseUrl;

	private String description;

	private String contactName;

	private String contactPhone;

	@Email(message = "邮箱格式不正确")
	private String contactEmail;

	@Schema(description = "AppKey")
	private String appKey;

	@Schema(description = "AppSecret")
	private String appSecret;


	private Boolean enable = true;

}