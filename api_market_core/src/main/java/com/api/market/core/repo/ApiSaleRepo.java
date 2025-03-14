package com.api.market.core.repo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.api.market.core.po.ApiPO;
import com.api.market.core.po.ApiSalePO;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Objects;

public interface ApiSaleRepo extends JpaRepository<ApiSalePO, Long>, JpaSpecificationExecutor<ApiSalePO> {

	default Page<ApiSalePO> search(String search, Boolean enable, Pageable pageable) {

		Specification<ApiSalePO> specification = new Specification<>() {
			final List<Predicate> predicates = CollUtil.newArrayList();

			public Predicate toPredicate(Root<ApiSalePO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (CharSequenceUtil.isNotBlank(search)) {
					predicates.add(cb.or(cb.like(root.get(ApiSalePO.Fields.api).get(ApiPO.Fields.name), "%" + search + "%"),
							cb.like(root.get(ApiPO.Fields.apiCode), "%" + search + "%")));
				}
				if (Objects.nonNull(enable)) {
					predicates.add(cb.equal(root.get(ApiSalePO.Fields.enable), enable));
				}
				return query.where(predicates.toArray(Predicate[]::new)).getRestriction();
			}
		};

		return this.findAll(specification, pageable);
	}

}