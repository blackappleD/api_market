package com.api.market.api.exception;

import com.api.market.core.exception.ApiMarketException;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/1/13 9:13
 */
public class ApiException extends ApiMarketException {

	private ApiException(String message) {
		super(message);
	}

	public static ApiException unknownApiCode() {
		return new ApiException("API未配置");
	}

	public static ApiException rateLimit() {
		return new ApiException("接口限流");
	}

	public static ApiException accessLimit() {
		return new ApiException("接口调用超限");
	}

}
