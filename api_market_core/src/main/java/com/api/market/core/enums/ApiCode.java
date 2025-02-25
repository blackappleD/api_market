package com.api.market.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/10 13:23
 */
@Getter
@AllArgsConstructor
public enum ApiCode {

	XHS_SEARCH_NOTE_LIST("xhs_search_note_list", "小红书搜索笔记列表"),
	TIK_TOK_SHOP_LIST("TIK_TOK_SHOP_LIST", "tiktok商城店铺产品列表"),

	UN_KNOWN("", "");

	private final String code;
	private final String message;

	public static ApiCode getByCode(String code) {
		for (ApiCode apiCode : ApiCode.values()) {
			if (apiCode.getCode().equals(code)) {
				return apiCode;
			}
		}
		return UN_KNOWN;
	}

}
