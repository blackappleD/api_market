package com.api.market.core.repo;

import com.api.market.core.po.MerchantApiLogPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MerchantApiLogRepo
        extends JpaRepository<MerchantApiLogPO, String>, JpaSpecificationExecutor<MerchantApiLogPO> {
}