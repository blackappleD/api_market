package com.api.market.core.dto.supplier;

import com.api.market.core.dto.base.LongIdNameDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SupplierApiResDTO {
	private Long id;

	private Boolean enable;

	private String name;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:MM:ss")
	private LocalDateTime createTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:MM:ss")
	private LocalDateTime updateTime;

	@Schema(description = "关联供应商")
	private LongIdNameDTO supplier;

	private Integer priority;

	@Schema(description = "关联api")
	private LongIdNameDTO api;

	@Schema(description = "进价")
	private BigDecimal price;

}