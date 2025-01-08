package com.api.market.core.dto.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApiUpdateReqDTO {
    @NotBlank(message = "ID不能为空")
    private String id;

    @NotBlank(message = "接口名称不能为空")
    private String apiName;

    private String categoryId;

    private String description;

    private String requestMethod;

    private String requestParams;

    private String responseParams;

    private Integer status;
}