package com.api.market.core.mapper;

import com.api.market.core.dto.category.ApiCategoryCreateReqDTO;
import com.api.market.core.dto.category.ApiCategoryResDTO;
import com.api.market.core.dto.category.ApiCategoryUpdateReqDTO;
import com.api.market.core.po.ApiCategoryPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ApiCategoryMapper extends BaseMapper<ApiCategoryPO, ApiCategoryResDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "1")
    ApiCategoryPO fromCreateDTO(ApiCategoryCreateReqDTO dto);

    void updateFromDTO(@MappingTarget ApiCategoryPO po, ApiCategoryUpdateReqDTO dto);
}