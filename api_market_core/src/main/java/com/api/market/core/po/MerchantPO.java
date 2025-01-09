package com.api.market.core.po;

import com.api.market.core.po.base.BasePO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = MerchantPO.TABLE_NAME)
@FieldNameConstants
public class MerchantPO extends BasePO.CommonPO<Long> {

	public static final String TABLE_NAME = "am_merchant";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, length = 50, unique = true)
	private String merCode;

	@Column(length = 50)
	private String contactName;

	@Column(length = 20)
	private String contactPhone;

	@Column(length = 100)
	private String contactEmail;

	@Column(nullable = false, length = 50, unique = true)
	private String appKey;

	@Column(nullable = false, length = 100)
	private String appSecret;

	@Column(nullable = false)
	private Boolean enable = true;

}