package com.api.market.core.po;

import com.api.market.core.po.base.BasePO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = ApiSalePO.TABLE_NAME)
@FieldNameConstants
public class ApiSalePO extends BasePO.CommonPO<Long> {

	public static final String TABLE_NAME = "am_api_sale";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private ApiPO api;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private MerchantPO merchant;

	@ManyToMany
	@JoinTable(joinColumns = @JoinColumn(name = "api_sale_id"),
			inverseJoinColumns = @JoinColumn(name = "router_supplier_id"),
			foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private List<SupplierPO> routerSuppliers;

	private Boolean enable;

}