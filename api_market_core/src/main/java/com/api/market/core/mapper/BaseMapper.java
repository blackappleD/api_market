package com.api.market.core.mapper;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface BaseMapper<PO, DTO> {

	DTO toDto(PO po);

	PO toPo(DTO dto);

	List<DTO> toDtoList(List<PO> poList);

	List<PO> toPoList(List<DTO> dtoList);

	void updatePo(@MappingTarget PO po, DTO dto);
}