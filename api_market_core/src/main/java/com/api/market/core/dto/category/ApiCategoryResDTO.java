package com.api.market.core.dto.category;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApiCategoryResDTO {
    private String id;
    private String categoryName;
    private String description;
    private Integer sort;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}