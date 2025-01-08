package com.api.market.core.po;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = ApiPO.TABLE_NAME)
public class ApiPO extends BasePO.CommonPO<Long> {

	public static final String TABLE_NAME = "am_api";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "api_code", nullable = false, length = 50, unique = true)
	private String apiCode;

	@Column(name = "api_name", nullable = false, length = 100)
	private String apiName;

	@Column(name = "category_id", nullable = false, length = 32)
	private String categoryId;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "request_method", nullable = false, length = 10)
	private String requestMethod;

	@Column(name = "request_params", columnDefinition = "TEXT")
	private String requestParams;

	@Column(name = "response_params", columnDefinition = "TEXT")
	private String responseParams;

	@Column(name = "status", nullable = false)
	private Integer status = 1;
}