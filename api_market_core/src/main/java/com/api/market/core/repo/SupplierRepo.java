package com.api.market.core.repo;

import com.api.market.core.po.SupplierPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SupplierRepo extends JpaRepository<SupplierPO, String>, JpaSpecificationExecutor<SupplierPO> {
    Optional<SupplierPO> findBySupplierCode(String supplierCode);
}