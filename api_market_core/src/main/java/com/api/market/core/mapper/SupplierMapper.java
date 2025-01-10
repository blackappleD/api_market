package com.api.market.core.mapper;

import com.api.market.core.dto.supplier.SupplierCreateReqDTO;
import com.api.market.core.dto.supplier.SupplierResDTO;
import com.api.market.core.dto.supplier.SupplierUpdateReqDTO;
import com.api.market.core.mapper.base.BaseAllMapper;
import com.api.market.core.po.SupplierPO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierMapper extends BaseAllMapper<SupplierPO, SupplierResDTO, SupplierCreateReqDTO, SupplierUpdateReqDTO> {

}