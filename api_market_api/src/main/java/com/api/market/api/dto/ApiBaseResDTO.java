package com.api.market.api.dto;

import com.api.market.core.enums.BillStatus;
import lombok.Data;

@Data
public class ApiBaseResDTO<T> {
	private Integer code;

	private String message;

	private BillStatus bill;

	private T data;

	public static <T> ApiBaseResDTO<T> success(T data) {
		ApiBaseResDTO<T> response = new ApiBaseResDTO<>();
		response.code = 200;
		response.data = data;
		response.bill = BillStatus.YES;
		response.message = "成功";
		return response;
	}

	public static <T> ApiBaseResDTO<T> fail(Integer code, String msg) {
		return fail(code, msg, null);
	}

	public static <T> ApiBaseResDTO<T> fail(Integer code, String msg, Throwable ex) {
		ApiBaseResDTO<T> response = new ApiBaseResDTO<>();
		response.code = code;
		response.message = msg;
		response.bill = BillStatus.NO;
		return response;
	}
}
