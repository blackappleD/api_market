package com.api.market.core.mapper;

import com.api.market.core.dto.supplier.SupplierApiCreateDTO;
import com.api.market.core.dto.supplier.SupplierApiResDTO;
import com.api.market.core.dto.supplier.SupplierApiUpdateDTO;
import com.api.market.core.mapper.base.BaseAllMapper;
import com.api.market.core.po.SupplierApiPO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierApiMapper extends BaseAllMapper<SupplierApiPO, SupplierApiResDTO, SupplierApiCreateDTO, SupplierApiUpdateDTO> {

}