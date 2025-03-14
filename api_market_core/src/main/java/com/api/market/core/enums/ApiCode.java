package com.api.market.core.enums;

import com.api.market.core.dto.base.StrIdNameDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/10 13:23
 */
@Getter
@AllArgsConstructor
public enum ApiCode {

	XHS_SEARCH_NOTE_LIST("XHS_SEARCH_NOTE_LIST", "小红书搜索笔记列表"),
	TIK_TOK_SHOP_LIST("TIK_TOK_SHOP_LIST", "tiktok商城店铺产品列表"),

	TEST("TEST", "测试接口"),
	UN_KNOWN("UN_KNOWN", "未知");

	private final String code;
	private final String message;

	public static List<StrIdNameDTO> allFields() {

		List<StrIdNameDTO> list = new ArrayList<>();
		for (ApiCode apiCode : ApiCode.values()) {
			StrIdNameDTO dto = new StrIdNameDTO();
			dto.setId(apiCode.getCode());
			dto.setName(apiCode.getMessage());
			list.add(dto);
		}
		return list;
	}

}
