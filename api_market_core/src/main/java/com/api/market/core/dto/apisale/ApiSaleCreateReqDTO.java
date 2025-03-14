package com.api.market.core.dto.apisale;

import com.api.market.core.dto.base.LongIdDTO;
import lombok.Data;

import java.util.List;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/2/25 17:19
 */
@Data
public class ApiSaleCreateReqDTO {

	private LongIdDTO api;

	private LongIdDTO merchant;

	private List<LongIdDTO> routerSuppliers;

	private Boolean enable;
}
