package com.api.market.core.mapper;

import com.api.market.core.dto.api.ApiCreateReqDTO;
import com.api.market.core.dto.api.ApiUpdateReqDTO;
import com.api.market.core.dto.category.ApiCategoryCreateReqDTO;
import com.api.market.core.dto.category.ApiCategoryResDTO;
import com.api.market.core.dto.category.ApiCategoryUpdateReqDTO;
import com.api.market.core.mapper.base.BaseAllMapper;
import com.api.market.core.po.ApiCategoryPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ApiCategoryMapper extends BaseAllMapper<ApiCategoryPO, ApiCategoryResDTO, ApiCreateReqDTO, ApiUpdateReqDTO> {

	@Mapping(target = "id", ignore = true)
	ApiCategoryPO fromCreateDTO(ApiCategoryCreateReqDTO dto);

	void fromUpdateDTO(@MappingTarget ApiCategoryPO po, ApiCategoryUpdateReqDTO dto);
}