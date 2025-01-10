package com.api.market.core.dto.supplier;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SupplierResDTO {
	private Long id;
	private String name;
	private String supCode;
	private String description;
	private String contactName;
	private String contactPhone;
	private String contactEmail;
	private Boolean enable;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}