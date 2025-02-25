package com.api.market.api.service;

import com.api.market.api.dto.ApiBaseReqDTO;
import com.api.market.api.dto.ApiBaseResDTO;
import com.api.market.api.dto.tkshop.TkShopStoreProductsReqDTO;
import com.api.market.api.dto.tkshop.TkShopStoreProductsResDTO;
import com.api.market.api.dto.xhs.XhsSearchNoteListReqDTO;
import com.api.market.api.dto.xhs.XhsSearchNoteListResDTO;
import com.api.market.api.exception.ApiException;
import com.api.market.core.enums.ApiCode;
import com.api.market.core.util.JsonUtil;
import lombok.Data;
import org.springframework.stereotype.Service;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/10 12:57
 */
@Service("Test")
public class TestService implements SupplierService {

	@Data
	public static class BaseResDTO<T> {
		private String code;
		private boolean success;
		private T data;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ApiBaseResDTO execute(ApiBaseReqDTO params) {

		return switch (ApiCode.getByCode(params.getApiCode())) {
			case XHS_SEARCH_NOTE_LIST ->
					xhsSearchNoteList(JsonUtil.fromJson(JsonUtil.toJson(params), XhsSearchNoteListReqDTO.class));
			case TIK_TOK_SHOP_LIST ->
					tkShopStoreProducts(JsonUtil.fromJson(JsonUtil.toJson(params), TkShopStoreProductsReqDTO.class));
			default -> throw ApiException.unknownApiCode();
		};
	}

	public ApiBaseResDTO<XhsSearchNoteListResDTO> xhsSearchNoteList(XhsSearchNoteListReqDTO dto) {
		return null;
	}

	public ApiBaseResDTO<TkShopStoreProductsResDTO> tkShopStoreProducts(TkShopStoreProductsReqDTO dto) {
		return null;
	}

}
