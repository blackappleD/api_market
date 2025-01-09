package com.api.market.core.po;

import com.api.market.core.po.base.BasePO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = SupplierApiPO.TABLE_NAME)
public class SupplierApiPO extends BasePO.CommonPO<Long> {

	public static final String TABLE_NAME = "am_supplier_api";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Comment("关联供应商")
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private SupplierPO supplier;

	@Comment("关联api")
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private ApiPO api;

	@Comment("进价")
	@Column(scale = 2)
	private BigDecimal price;

	@Comment("是否启用")
	private boolean enable = true;

}