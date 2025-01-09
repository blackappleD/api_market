package com.api.market.core.repo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.api.market.core.po.ApiCategoryPO;
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

public interface ApiCategoryRepo extends JpaRepository<ApiCategoryPO, Long>, JpaSpecificationExecutor<ApiCategoryPO> {

	default Page<ApiCategoryPO> search(String search, Boolean enable, Pageable pageable) {

		Specification<ApiCategoryPO> specification = new Specification<>() {
			final List<Predicate> predicates = CollUtil.newArrayList();

			public Predicate toPredicate(Root<ApiCategoryPO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (CharSequenceUtil.isNotBlank(search)) {
					List<String> searchList = List.of(search.split(" "));
					searchList.forEach(s -> predicates.add(cb.or(cb.like(root.get(ApiCategoryPO.Fields.name), "%" + s + "%"))));
				}
				if (Objects.nonNull(enable)) {
					predicates.add(cb.equal(root.get(ApiCategoryPO.Fields.enable), enable));
				}
				return query.where(predicates.toArray(Predicate[]::new)).getRestriction();
			}
		};
		return this.findAll(specification, pageable);
	}
}