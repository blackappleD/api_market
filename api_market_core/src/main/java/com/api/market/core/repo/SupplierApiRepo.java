package com.api.market.core.repo;

import cn.hutool.core.collection.CollUtil;
import com.api.market.core.dto.supplier.SupplierApiQueryReqDTO;
import com.api.market.core.po.ApiPO;
import com.api.market.core.po.SupplierApiPO;
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
import java.util.Optional;

public interface SupplierApiRepo extends JpaRepository<SupplierApiPO, Long>, JpaSpecificationExecutor<SupplierApiPO> {

	Optional<SupplierApiPO> findBySupplierAndApi(SupplierPO supplier, ApiPO api);

	List<SupplierApiPO> findAllByApi(ApiPO api);

	default Page<SupplierApiPO> search(SupplierApiQueryReqDTO dto, Pageable pageable) {
		Specification<SupplierApiPO> specification = new Specification<>() {
			final List<Predicate> predicates = CollUtil.newArrayList();

			public Predicate toPredicate(Root<SupplierApiPO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (Objects.nonNull(dto.getSupplierId())) {
					predicates.add(cb.equal(root.get(SupplierApiPO.Fields.supplier).get(SupplierPO.Fields.id), dto.getSupplierId()));
				}
				if (Objects.nonNull(dto.getApiId())) {
					predicates.add(cb.equal(root.get(SupplierApiPO.Fields.api).get(ApiPO.Fields.id), dto.getApiId()));
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