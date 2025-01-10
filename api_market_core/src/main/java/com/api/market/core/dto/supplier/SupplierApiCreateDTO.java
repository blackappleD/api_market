package com.api.market.core.dto.supplier;

import com.api.market.core.dto.base.LongIdDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/10 14:12
 */
@Data
public class SupplierApiCreateDTO {

	@Schema(description = "关联供应商")
	private LongIdDTO supplier;

	@Schema(description = "关联api")
	private LongIdDTO api;

	@Schema(description = "进价")
	private BigDecimal price;

	@Schema(description = "是否启用")
	private boolean enable = true;

}
