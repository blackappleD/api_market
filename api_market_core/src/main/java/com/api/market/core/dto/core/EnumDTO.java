package com.api.market.core.dto.core;

import com.api.market.core.dto.base.StrIdNameDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/3/14 13:43
 */
@Data
public class EnumDTO {

	@Schema(description = "枚举key")
	private String key;

	@Schema(description = "枚举内容")
	private List<StrIdNameDTO> fields;

}
