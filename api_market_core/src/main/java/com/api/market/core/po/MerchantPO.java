package com.api.market.core.po;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = MerchantPO.TABLE_NAME)
public class MerchantPO extends BasePO.CommonPO<Long> {

	public static final String TABLE_NAME = "am_merchant";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "merchant_name", nullable = false, length = 100)
	private String merchantName;

	@Column(name = "merchant_code", nullable = false, length = 50, unique = true)
	private String merchantCode;

	@Column(name = "contact_name", length = 50)
	private String contactName;

	@Column(name = "contact_phone", length = 20)
	private String contactPhone;

	@Column(name = "contact_email", length = 100)
	private String contactEmail;

	@Column(name = "app_key", nullable = false, length = 50, unique = true)
	private String appKey;

	@Column(name = "app_secret", nullable = false, length = 100)
	private String appSecret;

	@Column(name = "status", nullable = false)
	private Integer status = 1;

}