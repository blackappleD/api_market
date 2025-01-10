package com.api.market.core.dto.api;

import com.api.market.core.dto.base.LongIdNameDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResDTO {

	private Long id;

	private String apiCode;

	private String name;

	private LongIdNameDTO category;

	private String description;

	private Boolean enable;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:MM:ss")
	private LocalDateTime createTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:MM:ss")
	private LocalDateTime updateTime;

}