package com.api.market.core.dto.merchant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MerchantCreateReqDTO {
	@NotBlank(message = "商户名称不能为空")
	private String name;

	@NotBlank(message = "商户编码不能为空")
	private String merCode;

	private String contactName;

	private String contactPhone;

	@NotBlank(message = "邮箱不能为空")
	@Email(message = "邮箱格式不正确")
	private String contactEmail;

}