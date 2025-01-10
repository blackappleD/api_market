package com.api.market.core.dto.api;

import com.api.market.core.dto.base.LongIdDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApiUpdateReqDTO {
	@NotNull(message = "ID不能为空")
	private Long id;

	@NotBlank(message = "接口名称不能为空")
	private String name;

	private LongIdDTO category = new LongIdDTO();

	private String description;

}