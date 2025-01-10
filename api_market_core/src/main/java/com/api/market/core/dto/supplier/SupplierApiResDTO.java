package com.api.market.core.dto.supplier;

import com.api.market.core.dto.base.LongIdNameDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SupplierApiResDTO {
	private Long id;

	private Boolean enable;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	@Schema(description = "关联供应商")
	private LongIdNameDTO supplier;

	@Schema(description = "关联api")
	private LongIdNameDTO api;

	@Schema(description = "进价")
	private BigDecimal price;

}