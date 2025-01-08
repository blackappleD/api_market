package com.api.market.core.dto.api;

import com.api.market.core.dto.base.BasePageReqDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiQueryReqDTO extends BasePageReqDTO {
    private String apiName;
    private String apiCode;
    private String categoryId;
    private Integer status;
}