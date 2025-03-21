package com.api.market.core.dto.supplier;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SupplierUpdateReqDTO {
	@NotNull(message = "ID不能为空")
	private Long id;

	@NotBlank(message = "供应商名称不能为空")
	private String name;

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

	private Boolean enable;
}