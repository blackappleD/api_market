package com.api.market.core.dto.merchant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MerchantUpdateReqDTO {
	@NotBlank(message = "ID不能为空")
	private Long id;

	@NotBlank(message = "商户名称不能为空")
	private String merchantName;

	private String contactName;

	private String contactPhone;

	@Email(message = "邮箱格式不正确")
	private String contactEmail;

	private Integer status;
}