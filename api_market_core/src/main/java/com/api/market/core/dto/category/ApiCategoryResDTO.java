package com.api.market.core.dto.category;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiCategoryResDTO {
	private String id;
	private String name;
	private String description;
	private Boolean enable;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:MM:ss")
	private LocalDateTime createTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:MM:ss")
	private LocalDateTime updateTime;
}