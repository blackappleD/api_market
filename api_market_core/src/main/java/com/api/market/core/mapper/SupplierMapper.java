package com.api.market.core.mapper;

import com.api.market.core.dto.supplier.SupplierCreateReqDTO;
import com.api.market.core.dto.supplier.SupplierResDTO;
import com.api.market.core.dto.supplier.SupplierUpdateReqDTO;
import com.api.market.core.po.SupplierPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SupplierMapper extends BaseMapper<SupplierPO, SupplierResDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "1")
    SupplierPO fromCreateDTO(SupplierCreateReqDTO dto);

    void fromUpdateDTO(@MappingTarget SupplierPO po, SupplierUpdateReqDTO dto);
}