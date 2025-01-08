package com.api.market.core.dto.api;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResDTO {
    private String id;
    private String apiCode;
    private String apiName;
    private String categoryId;
    private String categoryName;
    private String description;
    private String requestMethod;
    private String requestParams;
    private String responseParams;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}