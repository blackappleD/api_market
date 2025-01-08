package com.api.market.core.mapper;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface BaseMapper<PO, DTO> {

	DTO toDTO(PO po);

	PO toPO(DTO dto);

	List<DTO> toDTOList(List<PO> poList);

	List<PO> toPOList(List<DTO> dtoList);

	void updatePO(@MappingTarget PO po, DTO dto);
}