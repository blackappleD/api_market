package com.api.market.core.exception;

public class ApiCategoryException extends ApiMarketException {
	private ApiCategoryException(String message) {
		super(message);
	}

	public static ApiCategoryException notFound() {
		return new ApiCategoryException("API分类不存在");
	}

}