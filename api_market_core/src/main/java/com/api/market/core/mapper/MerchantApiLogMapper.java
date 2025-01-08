package com.api.market.core.mapper;

import com.api.market.core.dto.merchantlog.MerchantApiLogResDTO;
import com.api.market.core.po.MerchantApiLogPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MerchantApiLogMapper extends BaseMapper<MerchantApiLogPO, MerchantApiLogResDTO> {

    @Override
    @Mapping(target = "merchantName", ignore = true)
    @Mapping(target = "apiName", ignore = true)
    MerchantApiLogResDTO toDTO(MerchantApiLogPO po);
}