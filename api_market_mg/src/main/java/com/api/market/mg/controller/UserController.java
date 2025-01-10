package com.api.market.mg.controller;

import com.api.market.core.annotations.PkAuthControl;
import com.api.market.core.annotations.PkAuthIgnore;
import com.api.market.core.annotations.PkResponseBody;
import com.api.market.core.auth.TokenInfo;
import com.api.market.core.dto.LoginReqDTO;
import com.api.market.core.dto.UserResDTO;
import com.api.market.core.service.UserService;
import com.api.market.core.util.AuthUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/12/20 9:26
 */
@RestController
@RequestMapping("/user")
@PkResponseBody
@PkAuthControl
public class UserController {

	@Resource
	private UserService userService;


	@GetMapping("/info")
	public UserResDTO userInfo() {
		return userService.get(AuthUtil.getLoginId());
	}

	@PkAuthIgnore
	@PostMapping("/login")
	public TokenInfo login(@RequestBody @Valid LoginReqDTO dto) {
		return userService.login(dto);
	}

	@PostMapping("/logout")
	public void logout() {
		userService.logout();
	}

}
