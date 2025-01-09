package com.api.market.core.repo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.api.market.core.dto.merchantlog.MerchantApiLogQueryReqDTO;
import com.api.market.core.po.MerchantApiLogPO;
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

public interface MerchantApiLogRepo extends JpaRepository<MerchantApiLogPO, Long>, JpaSpecificationExecutor<MerchantApiLogPO> {

	default Page<MerchantApiLogPO> search(MerchantApiLogQueryReqDTO dto, Pageable pageable) {
		Specification<MerchantApiLogPO> specification = new Specification<>() {
			final List<Predicate> predicates = CollUtil.newArrayList();

			public Predicate toPredicate(Root<MerchantApiLogPO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (CharSequenceUtil.isNotBlank(dto.getResCode())) {
					predicates.add(cb.equal(root.get(MerchantApiLogPO.Fields.resCode), dto.getResCode()));
				}
				if (Objects.nonNull(dto.getMerchantId())) {
					predicates.add(cb.equal(root.get(MerchantApiLogPO.Fields.merchantId), dto.getMerchantId()));
				}
				if (Objects.nonNull(dto.getApiId())) {
					predicates.add(cb.equal(root.get(MerchantApiLogPO.Fields.apiId), dto.getApiId()));
				}
				if (Objects.nonNull(dto.getStartTime()) && Objects.nonNull(dto.getEndTime())) {
					predicates.add(cb.between(root.get(MerchantApiLogPO.Fields.requestTime), dto.getStartTime(), dto.getEndTime()));
				}
				return query.where(predicates.toArray(Predicate[]::new)).getRestriction();
			}
		};
		return this.findAll(specification, pageable);
	}

}