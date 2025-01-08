package com.api.market.core.auth;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TokenInfo {
    private String tokenValue;

    private String userId;

    private LocalDateTime expireAt;
}
