package com.api.market.api.service;

import com.api.market.api.dto.ApiBaseReqDTO;
import com.api.market.api.dto.ApiBaseResDTO;
import com.api.market.api.dto.TestReqDTO;
import com.api.market.api.dto.tkshop.TkShopStoreProductsReqDTO;
import com.api.market.api.dto.tkshop.TkShopStoreProductsResDTO;
import com.api.market.api.dto.xhs.XhsSearchNoteListReqDTO;
import com.api.market.api.dto.xhs.XhsSearchNoteListResDTO;
import com.api.market.api.exception.ApiException;
import com.api.market.core.po.SupplierApiPO;
import com.api.market.core.util.JsonUtil;
import lombok.Data;
import org.springframework.stereotype.Service;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/10 12:57
 */
@Service("ddkj")
public class DdkjService implements SupplierService {

	@Data
	public static class BaseResDTO<T> {
		private String code;
		private boolean success;
		private T data;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ApiBaseResDTO execute(SupplierApiPO supApi, ApiBaseReqDTO dto) {
		Object params = dto.getParams();
		String paramsStr = JsonUtil.toJson(params);
		return switch (dto.getApiCode()) {
			case XHS_SEARCH_NOTE_LIST -> xhsSearchNoteList(JsonUtil.fromJson(paramsStr, XhsSearchNoteListReqDTO.class));
			case TIK_TOK_SHOP_LIST ->
					tkShopStoreProducts(JsonUtil.fromJson(paramsStr, TkShopStoreProductsReqDTO.class));
			case TEST -> test(JsonUtil.fromJson(paramsStr, TestReqDTO.class));
			default -> throw ApiException.unknownApiCode();
		};
	}

	public ApiBaseResDTO<XhsSearchNoteListResDTO> xhsSearchNoteList(XhsSearchNoteListReqDTO dto) {
		return null;
	}

	public ApiBaseResDTO<TkShopStoreProductsResDTO> tkShopStoreProducts(TkShopStoreProductsReqDTO dto) {
		return null;
	}

	public ApiBaseResDTO<String> test(TestReqDTO dto) {

		return ApiBaseResDTO.success("success");
	}

}
