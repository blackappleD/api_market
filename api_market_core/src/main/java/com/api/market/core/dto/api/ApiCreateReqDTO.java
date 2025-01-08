package com.api.market.core.dto.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApiCreateReqDTO {
    @NotBlank(message = "接口编码不能为空")
    private String apiCode;

    @NotBlank(message = "接口名称不能为空")
    private String apiName;

    @NotBlank(message = "分类ID不能为空")
    private String categoryId;

    private String description;

    @NotBlank(message = "请求方法不能为空")
    private String requestMethod;

    private String requestParams;

    private String responseParams;
}