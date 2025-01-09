package com.api.market.core.dto.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupplierUpdateReqDTO {
	@NotBlank(message = "ID不能为空")
	private Long id;

	@NotBlank(message = "供应商名称不能为空")
	private String name;

	private String description;

	private String contactName;

	private String contactPhone;

	@Email(message = "邮箱格式不正确")
	private String contactEmail;

	private Boolean enable;
}