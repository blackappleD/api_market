package com.api.market.core.service;

import com.api.market.core.dto.base.PageDTO;
import com.api.market.core.dto.supplier.*;
import com.api.market.core.exception.SupplierException;
import com.api.market.core.jpa.PkPageable;
import com.api.market.core.mapper.SupplierApiMapper;
import com.api.market.core.po.SupplierApiPO;
import com.api.market.core.repo.SupplierApiRepo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SupplierApiService {

	@Resource
	private SupplierApiRepo supplierApiRepo;

	@Resource
	private SupplierApiMapper supplierApiMapper;

	public Long create(SupplierApiCreateDTO req) {

		SupplierApiPO po = supplierApiMapper.fromCreateDTO(req);
		return supplierApiRepo.save(po).getId();

	}

	public void update(SupplierApiUpdateDTO req) {
		SupplierApiPO po = findById(req.getId());
		supplierApiMapper.fromUpdateDTO(po, req);
	}

	public PageDTO<SupplierApiResDTO> search(SupplierApiQueryReqDTO req) {

		Page<SupplierApiPO> pages = supplierApiRepo.search(req, PkPageable.ofDefaultSort(req.getPage(), req.getSize()));

		return PageDTO.from(pages, po -> supplierApiMapper.toDto(po));

	}

	public SupplierApiPO findById(Long id) {
		return supplierApiRepo.findById(id).orElseThrow(() -> new SupplierException("尚未绑定该api"));
	}

}