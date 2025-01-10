package com.api.market.core.mapper;

import com.api.market.core.dto.merchant.MerchantCreateReqDTO;
import com.api.market.core.dto.merchant.MerchantResDTO;
import com.api.market.core.dto.merchant.MerchantUpdateReqDTO;
import com.api.market.core.po.MerchantPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MerchantMapper extends BaseMapper<MerchantPO, MerchantResDTO> {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "appKey", ignore = true)
	@Mapping(target = "appSecret", ignore = true)
	MerchantPO fromCreateDTO(MerchantCreateReqDTO dto);

	@Mapping(target = "appKey", ignore = true)
	@Mapping(target = "appSecret", ignore = true)
	void fromUpdateDTO(@MappingTarget MerchantPO po, MerchantUpdateReqDTO dto);
}