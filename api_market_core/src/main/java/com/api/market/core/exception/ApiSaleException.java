package com.api.market.core.exception;

public class ApiSaleException extends ApiMarketException {
	private ApiSaleException(String message) {
		super(message);
	}

	public static ApiSaleException notFound() {
		return new ApiSaleException("商户暂未配置该接口");
	}

	public static ApiSaleException alreadyExist() {
		return new ApiSaleException("商户已配置该接口");
	}

}