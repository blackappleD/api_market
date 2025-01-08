package com.api.market.core.mapper;

import com.api.market.core.dto.api.ApiCreateReqDTO;
import com.api.market.core.dto.api.ApiResDTO;
import com.api.market.core.dto.api.ApiUpdateReqDTO;
import com.api.market.core.po.ApiPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ApiMapper extends BaseMapper<ApiPO, ApiResDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "1")
    ApiPO fromCreateDTO(ApiCreateReqDTO dto);

    void updateFromDTO(@MappingTarget ApiPO po, ApiUpdateReqDTO dto);
}