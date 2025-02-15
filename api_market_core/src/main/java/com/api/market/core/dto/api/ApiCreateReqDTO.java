package com.api.market.core.dto.api;

import com.api.market.core.dto.base.LongIdDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApiCreateReqDTO {
	@NotBlank(message = "接口编码不能为空")
	private String apiCode;

	@NotBlank(message = "接口名称不能为空")
	private String name;

	@NotNull(message = "分类不能为空")
	private LongIdDTO category;

	private String description;

}