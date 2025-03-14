package com.api.market.core.dto.apisale;

import com.api.market.core.dto.base.LongIdDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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

	private LongIdDTO api;

	private LongIdDTO merchant;

	private List<LongIdDTO> routerSuppliers;

	private Boolean enable;

}
