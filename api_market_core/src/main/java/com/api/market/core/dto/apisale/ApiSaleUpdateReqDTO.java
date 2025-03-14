package com.api.market.core.dto.apisale;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/2/25 17:20
 */
@Data
public class ApiSaleUpdateReqDTO {

	@NotNull(message = "ID不能为空")
	private Long id;

	// todo 补充其他字段
}
