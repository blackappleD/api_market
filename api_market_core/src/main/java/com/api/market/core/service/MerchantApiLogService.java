package com.api.market.core.service;

import com.api.market.core.dto.base.PageDTO;
import com.api.market.core.dto.merchantlog.MerchantApiLogQueryReqDTO;
import com.api.market.core.dto.merchantlog.MerchantApiLogResDTO;
import com.api.market.core.jpa.PkPageable;
import com.api.market.core.mapper.MerchantApiLogMapper;
import com.api.market.core.po.MerchantApiLogPO;
import com.api.market.core.repo.MerchantApiLogRepo;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class MerchantApiLogService {

	@Resource
	private MerchantApiLogRepo merchantApiLogRepo;


	@Resource
	private MerchantApiLogMapper merchantApiLogMapper;

	public PageDTO<MerchantApiLogResDTO> page(MerchantApiLogQueryReqDTO dto) {

		Page<MerchantApiLogPO> pages = merchantApiLogRepo.search(dto, PkPageable.ofDefaultSort(dto.getPage(), dto.getSize()));
		return PageDTO.from(pages, po -> merchantApiLogMapper.toDto(po));

	}
}