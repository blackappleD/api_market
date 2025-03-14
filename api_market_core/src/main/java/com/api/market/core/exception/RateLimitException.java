package com.api.market.core.exception;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/3/14 11:07
 */
public class RateLimitException extends ApiMarketException {
	private RateLimitException(String message) {
		super(message);
	}

	public static RateLimitException perDataLimit() {
		return new RateLimitException("当日调用量超过限制");
	}

	public static RateLimitException rateLimit() {
		return new RateLimitException("调用过于频繁，接口限流");
	}

}
