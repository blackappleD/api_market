package com.api.market.api.dto.xhs;

import lombok.Data;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/13 9:28
 */
@Data
public class XhsSearchNoteListReqDTO {

	private String keyword;

	private String page;

	// general popularity_descending time_descending 综合 最热 最新
	private String sort;

	// 默认为空, normal 图文筛选 video 视频筛选
	private String noteType;

}
