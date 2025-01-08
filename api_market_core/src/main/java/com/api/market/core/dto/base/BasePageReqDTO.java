package com.api.market.core.dto.base;

import lombok.Data;

@Data
public class BasePageReqDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String sortField;
    private String sortOrder; // asc, desc
}