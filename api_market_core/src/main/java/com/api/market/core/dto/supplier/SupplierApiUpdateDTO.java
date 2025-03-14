package com.api.market.core.dto.supplier;

import com.api.market.core.dto.base.LongIdDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/10 14:12
 */
@Data
public class SupplierApiUpdateDTO {

	@NotNull(message = "主键ID不能为空")
	private Long id;

	@Schema(description = "关联供应商")
	private LongIdDTO supplier;

	@Schema(description = "关联api")
	private LongIdDTO api;

	@Schema(description = "进价")
	private BigDecimal price;

	@Schema(description = "Api优先级")
	private Integer priority = 999;

	@Schema(description = "是否启用")
	private boolean enable = true;

}
