package com.api.market.core.repo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.api.market.core.dto.merchant.MerchantQueryReqDTO;
import com.api.market.core.po.MerchantPO;
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

public interface MerchantRepo extends JpaRepository<MerchantPO, Long>, JpaSpecificationExecutor<MerchantPO> {

	boolean existsByMerCode(String merCode);

	default Page<MerchantPO> search(MerchantQueryReqDTO dto, Pageable pageable) {
		Specification<MerchantPO> specification = new Specification<>() {
			final List<Predicate> predicates = CollUtil.newArrayList();

			public Predicate toPredicate(Root<MerchantPO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (CharSequenceUtil.isNotBlank(dto.getName())) {
					predicates.add(cb.like(root.get(MerchantPO.Fields.name), "%" + dto.getName() + "%"));
				}
				if (CharSequenceUtil.isNotBlank(dto.getMerCode())) {
					predicates.add(cb.like(root.get(MerchantPO.Fields.merCode), "%" + dto.getMerCode() + "%"));
				}
				if (Objects.nonNull(dto.getEnable())) {
					predicates.add(cb.equal(root.get(MerchantPO.Fields.enable), dto.getEnable()));
				}
				return query.where(predicates.toArray(Predicate[]::new)).getRestriction();
			}
		};
		return this.findAll(specification, pageable);
	}

}