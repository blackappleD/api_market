package com.api.market.core.po;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = MerchantApiLogPO.TABLE_NAME)
public class MerchantApiLogPO extends BasePO.CommonPO<Long> {

	public static final String TABLE_NAME = "am_merchant_api_log";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long merchantId;

	@Column(nullable = false)
	private String apiId;

	@Column(nullable = false)
	private LocalDateTime requestTime;

	@Column(nullable = false)
	private LocalDateTime responseTime;

	@Column(nullable = false)
	private Integer duration;

	@Column(columnDefinition = "TEXT")
	private String requestParams;

	@Column(columnDefinition = "TEXT")
	private String responseData;

	@Column(nullable = false)
	private Integer status;

	@Column(length = 500)
	private String errorMsg;

}