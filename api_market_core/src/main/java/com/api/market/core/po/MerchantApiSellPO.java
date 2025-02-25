package com.api.market.core.po;

import com.api.market.core.po.base.BasePO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = MerchantApiSellPO.TABLE_NAME)
@FieldNameConstants
public class MerchantApiSellPO extends BasePO.CommonPO<Long> {

	public static final String TABLE_NAME = "am_merchant_api_sell";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private ApiPO api;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private MerchantPO merchant;

	private boolean enable;

}