package com.api.market.core.dto.apisale;

import com.api.market.core.dto.base.BasePageReqDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/2/25 17:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiSaleQueryReqDTO extends BasePageReqDTO {

	private Boolean enable;
}
