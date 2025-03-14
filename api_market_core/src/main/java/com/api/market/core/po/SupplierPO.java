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
@Table(name = SupplierPO.TABLE_NAME)
@FieldNameConstants
public class SupplierPO extends BasePO.CommonPO<Long> {

	public static final String TABLE_NAME = "am_supplier";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Comment("供应商名称")
	@Column(nullable = false, length = 100)
	private String name;

	@Comment("供应商编码")
	@Column(nullable = false, length = 50, unique = true)
	private String supCode;

	@Comment("备注/描述")
	@Column(length = 1000)
	private String description;

	@Comment("供应商联系人姓名")
	@Column(length = 50)
	private String contactName;

	@Comment("供应商联系电话")
	@Column(length = 20)
	private String contactPhone;

	@Comment("供应商联系邮箱")
	@Column(length = 100)
	private String contactEmail;

	@Comment("供应商AppKey")
	@Column(length = 1000)
	private String appKey;

	@Comment("供应商AppSecret")
	@Column(length = 1000)
	private String appSecret;

	@Comment("供应商优先级，值越大优先级越低")
	@Column(length = 100)
	private Integer priority = 999;

	@Comment("是否启用")
	@Column(nullable = false)
	private Boolean enable = true;

}