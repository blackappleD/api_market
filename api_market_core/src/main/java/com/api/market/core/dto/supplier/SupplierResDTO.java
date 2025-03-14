package com.api.market.core.dto.supplier;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SupplierResDTO {
	private Long id;
	private String name;
	private String supCode;
	private String baseUrl;
	private String description;
	private String contactName;
	private String contactPhone;
	private String contactEmail;
	private Boolean enable;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:MM:ss")
	private LocalDateTime createTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:MM:ss")
	private LocalDateTime updateTime;
}