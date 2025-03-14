package com.api.market.core.mapper;

import com.api.market.core.dto.apisale.ApiSaleCreateReqDTO;
import com.api.market.core.dto.apisale.ApiSaleResDTO;
import com.api.market.core.dto.apisale.ApiSaleUpdateReqDTO;
import com.api.market.core.mapper.base.BaseAllMapper;
import com.api.market.core.po.ApiSalePO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ApiCategoryMapper.class)
public interface ApiSaleMapper extends BaseAllMapper<ApiSalePO, ApiSaleResDTO, ApiSaleCreateReqDTO, ApiSaleUpdateReqDTO> {

}