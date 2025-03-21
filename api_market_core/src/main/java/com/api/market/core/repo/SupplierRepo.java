package com.api.market.core.repo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.api.market.core.dto.supplier.SupplierQueryReqDTO;
import com.api.market.core.po.SupplierPO;
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

public interface SupplierRepo extends JpaRepository<SupplierPO, Long>, JpaSpecificationExecutor<SupplierPO> {
	Boolean existsBySupCode(String supCode);

	default Page<SupplierPO> search(SupplierQueryReqDTO dto, Pageable pageable) {
		Specification<SupplierPO> specification = new Specification<>() {
			final List<Predicate> predicates = CollUtil.newArrayList();

			public Predicate toPredicate(Root<SupplierPO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (CharSequenceUtil.isNotBlank(dto.getSearch())) {
					predicates.add(cb.like(root.get(SupplierPO.Fields.name), "%" + dto.getSearch() + "%"));
				}
				if (CharSequenceUtil.isNotBlank(dto.getSupCode())) {
					predicates.add(cb.like(root.get(SupplierPO.Fields.supCode), "%" + dto.getSupCode() + "%"));
				}
				if (Objects.nonNull(dto.getEnable())) {
					predicates.add(cb.equal(root.get(SupplierPO.Fields.enable), dto.getEnable()));
				}
				return query.where(predicates.toArray(Predicate[]::new)).getRestriction();
			}
		};
		return this.findAll(specification, pageable);
	}
}