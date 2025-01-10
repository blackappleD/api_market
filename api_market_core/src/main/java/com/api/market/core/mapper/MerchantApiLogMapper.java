package com.api.market.core.mapper;

import com.api.market.core.dto.merchantlog.MerchantApiLogResDTO;
import com.api.market.core.mapper.base.BaseResMapper;
import com.api.market.core.po.MerchantApiLogPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MerchantApiLogMapper extends BaseResMapper<MerchantApiLogPO, MerchantApiLogResDTO> {
	@Override
	@Mapping(target = "merchantName", ignore = true)
	@Mapping(target = "apiName", ignore = true)
	MerchantApiLogResDTO toDto(MerchantApiLogPO po);
}