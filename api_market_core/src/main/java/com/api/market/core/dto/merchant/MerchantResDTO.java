package com.api.market.core.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MerchantResDTO {
	private String id;
	private String name;
	private String merCode;
	private String contactName;
	private String contactPhone;
	private String contactEmail;
	private String appKey;
	private String appSecret;
	private Boolean enable;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:MM:ss")
	private LocalDateTime createTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:MM:ss")
	private LocalDateTime updateTime;
}