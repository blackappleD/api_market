package com.api.market.core.po;

import com.api.market.core.po.base.BasePO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Comment;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@FieldNameConstants
@Table(name = ApiCategoryPO.TABLE_NAME)
public class ApiCategoryPO extends BasePO.CommonPO<Long> {

	public static final String TABLE_NAME = "am_api_category";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Comment("接口分类名称")
	@Column(nullable = false, length = 50)
	private String name;

	@Column(length = 500)
	@Comment("描述")
	private String description;

	@Column(nullable = false)
	private Boolean enable = true;

}