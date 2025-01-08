package com.api.market.core.repo;

import com.api.market.core.po.MerchantPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MerchantRepo extends JpaRepository<MerchantPO, Long>, JpaSpecificationExecutor<MerchantPO> {
	Optional<MerchantPO> findByMerchantCode(String merchantCode);

	Optional<MerchantPO> findByAppKey(String appKey);

}