package com.api.market.core.po;

import com.api.market.core.po.base.BasePO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@FieldNameConstants
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
	@Column(precision = 32, scale = 2)
	private BigDecimal price;

	@Comment("api调用优先级，值越大优先级越低")
	@Column(length = 10)
	private Integer priority = 999;

	@Comment("是否启用")
	private boolean enable = true;

	public String getName() {
		return this.supplier.getName();
	}

}