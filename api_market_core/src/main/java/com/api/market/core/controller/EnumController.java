package com.api.market.core.controller;

import com.api.market.core.annotations.PkAuthControl;
import com.api.market.core.annotations.PkResponseBody;
import com.api.market.core.dto.core.EnumDTO;
import com.api.market.core.enums.ApiCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/3/14 13:41
 */
@RestController
@RequestMapping("/enum")
@PkResponseBody
@PkAuthControl
public class EnumController {

	// todo
	@GetMapping("/list")
	public List<EnumDTO> getAllEnum() {

		EnumDTO enumDto = new EnumDTO();
		enumDto.setKey("ApiCode");
		enumDto.setFields(ApiCode.allFields());
		return List.of(enumDto);

	}

}
