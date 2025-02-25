package com.api.market.core.po;

import com.api.market.core.po.base.BasePO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = MerchantApiLogPO.TABLE_NAME)
@FieldNameConstants
public class MerchantApiLogPO extends BasePO.CommonPO<Long> {

	public static final String TABLE_NAME = "am_merchant_api_log";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String seqNo;

	@Column(nullable = false)
	private Long merchantId;

	@Column(nullable = false, length = 60)
	private String merchantName;

	@Column(nullable = false)
	private Long apiId;

	@Column(nullable = false)
	private String apiName;

	@Column(nullable = false)
	private LocalDateTime requestTime;

	@Column(nullable = false)
	private LocalDateTime responseTime;

	@Column(nullable = false)
	private Integer duration;

	@Column(columnDefinition = "TEXT")
	private String requestJson;

	@Column(columnDefinition = "TEXT")
	private String responseJson;

	@Column(nullable = false)
	private String resCode;

	@Column(length = 500)
	private String errorMsg;

}