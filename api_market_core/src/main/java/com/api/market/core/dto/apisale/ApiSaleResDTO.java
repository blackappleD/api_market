package com.api.market.core.dto.apisale;

import com.api.market.core.dto.base.LongIdNameDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/2/25 17:20
 */
@Data
public class ApiSaleResDTO {

	private Long id;

	private LongIdNameDTO api;

	private BigDecimal price;

	private LongIdNameDTO merchant;

	private List<LongIdNameDTO> routerSupplierApis;

	private Boolean enable;

}
