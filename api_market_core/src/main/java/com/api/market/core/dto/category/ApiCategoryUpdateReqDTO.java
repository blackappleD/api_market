package com.api.market.core.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApiCategoryUpdateReqDTO {
	@NotBlank(message = "ID不能为空")
	private Long id;

	@NotBlank(message = "分类名称不能为空")
	private String name;

	private String description;

}