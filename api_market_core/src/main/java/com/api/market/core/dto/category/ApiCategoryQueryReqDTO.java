package com.api.market.core.dto.category;

import com.api.market.core.dto.base.BasePageReqDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiCategoryQueryReqDTO extends BasePageReqDTO {
    private String categoryName;
    private Integer status;
}