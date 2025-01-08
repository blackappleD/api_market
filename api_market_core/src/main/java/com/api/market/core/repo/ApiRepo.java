package com.api.market.core.repo;

import com.api.market.core.po.ApiPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ApiRepo extends JpaRepository<ApiPO, String>, JpaSpecificationExecutor<ApiPO> {
    Optional<ApiPO> findByApiCode(String apiCode);

    List<ApiPO> findByCategoryIdAndStatus(String categoryId, Integer status);
}