package com.api.market.core.mapper;

import com.api.market.core.po.UserPO;
import com.api.market.core.dto.UserResDTO;
import org.mapstruct.Mapper;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2024/12/20 9:38
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

	UserResDTO po2dto(UserPO po);

}
