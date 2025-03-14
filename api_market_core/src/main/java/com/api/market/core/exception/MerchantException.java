package com.api.market.core.exception;

import cn.hutool.core.text.CharSequenceUtil;
import com.api.market.core.enums.ApiCode;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/8 14:43
 */
public class MerchantException extends ApiMarketException {

	private MerchantException(int code, String message) {
		super(code, message);
	}

	private MerchantException(String message) {
		super(message);
	}

	public static MerchantException merCodeExist() {
		return new MerchantException(PARAM_CODE, "商户编码已存在");
	}

	public static MerchantException notFound() {
		return new MerchantException("商户不存在");
	}

	public static MerchantException merchantNotAvailable() {
		return new MerchantException("商户暂时不可用");
	}

	public static MerchantException merchantApiNotAvailable(String merchantCode, ApiCode apiCode) {
		return new MerchantException(CharSequenceUtil.format("商户 {} 暂时无法调用 {} 接口", merchantCode, apiCode.getMessage()));
	}

	public static MerchantException merchantApiRateLimit() {
		return new MerchantException("商户");
	}
}
