package com.api.market.core.dto.apisale;

import com.api.market.core.dto.base.LongIdDTO;
import jakarta.validation.constraints.NotNull;
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
public class ApiSaleUpdateReqDTO {

	@NotNull(message = "ID不能为空")
	private Long id;

	@NotNull(message = "api不能为空")
	private LongIdDTO api;

	@NotNull(message = "商户不能为空")
	private LongIdDTO merchant;

	@NotNull(message = "价格不能为空")
	private BigDecimal price;

	@NotNull(message = "供应商路由api不能为空")
	private List<LongIdDTO> routerSupplierApis;

	private Boolean enable;

}
