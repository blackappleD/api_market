package com.api.market.core.dto.merchantlog;

import com.api.market.core.dto.base.BasePageReqDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class MerchantApiLogQueryReqDTO extends BasePageReqDTO {
    private String merchantId;
    private String apiId;
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}