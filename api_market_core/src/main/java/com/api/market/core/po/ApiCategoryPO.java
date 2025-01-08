package com.api.market.core.po;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = ApiCategoryPO.TABLE_NAME)
public class ApiCategoryPO extends BasePO.CommonPO<Long> {

	public static final String TABLE_NAME = "am_api_category";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "category_name", nullable = false, length = 50)
	private String categoryName;

	@Column(name = "parent_id", length = 32)
	private String parentId;

	@Column(name = "sort_order", nullable = false)
	private Integer sortOrder = 0;

	@Column(name = "status", nullable = false)
	private Integer status = 1;

}