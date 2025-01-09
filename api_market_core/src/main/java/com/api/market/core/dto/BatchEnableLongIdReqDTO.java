package com.api.market.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/9 10:04
 */
@Data
public class BatchEnableLongIdReqDTO {

	@Schema(description = "需要批量修改状态的id列表")
	private List<Long> ids;

	private Boolean enable;

}
