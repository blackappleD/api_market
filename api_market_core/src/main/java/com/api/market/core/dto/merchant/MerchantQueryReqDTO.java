package com.api.market.core.dto.merchant;

import com.api.market.core.dto.base.BasePageReqDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MerchantQueryReqDTO extends BasePageReqDTO {
    private String merchantName;
    private String merchantCode;
    private String contactName;
    private Integer status;
}