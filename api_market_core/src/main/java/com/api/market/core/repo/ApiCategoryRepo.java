package com.api.market.core.repo;

import com.api.market.core.po.ApiCategoryPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ApiCategoryRepo extends JpaRepository<ApiCategoryPO, String>, JpaSpecificationExecutor<ApiCategoryPO> {
    List<ApiCategoryPO> findByParentIdOrderBySortOrderAsc(String parentId);
}