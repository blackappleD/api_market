package com.api.market.core.po;

import com.api.market.core.po.base.BasePO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@FieldNameConstants
@Table(name = ApiPO.TABLE_NAME)
public class ApiPO extends BasePO.CommonPO<Long> {

	public static final String TABLE_NAME = "am_api";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50, unique = true)
	private String apiCode;

	@Column(nullable = false, length = 100)
	private String name;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private ApiCategoryPO category;

	@Column(length = 1000)
	private String description;

	@Column(nullable = false)
	private Boolean enable = true;
}