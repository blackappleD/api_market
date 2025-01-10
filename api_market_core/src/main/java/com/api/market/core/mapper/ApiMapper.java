package com.api.market.core.mapper;

import com.api.market.core.dto.api.ApiCreateReqDTO;
import com.api.market.core.dto.api.ApiResDTO;
import com.api.market.core.dto.api.ApiUpdateReqDTO;
import com.api.market.core.mapper.base.BaseAllMapper;
import com.api.market.core.po.ApiPO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ApiCategoryMapper.class)
public interface ApiMapper extends BaseAllMapper<ApiPO, ApiResDTO, ApiCreateReqDTO, ApiUpdateReqDTO> {

}