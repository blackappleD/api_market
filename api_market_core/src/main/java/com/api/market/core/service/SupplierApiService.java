package com.api.market.core.service;

import com.api.market.core.dto.base.PageDTO;
import com.api.market.core.dto.supplier.SupplierApiCreateDTO;
import com.api.market.core.dto.supplier.SupplierApiQueryReqDTO;
import com.api.market.core.dto.supplier.SupplierApiResDTO;
import com.api.market.core.dto.supplier.SupplierApiUpdateDTO;
import com.api.market.core.exception.SupplierException;
import com.api.market.core.jpa.PkPageable;
import com.api.market.core.mapper.SupplierApiMapper;
import com.api.market.core.po.ApiPO;
import com.api.market.core.po.SupplierApiPO;
import com.api.market.core.po.SupplierPO;
import com.api.market.core.repo.SupplierApiRepo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SupplierApiService {

	@Resource
	private SupplierApiRepo supplierApiRepo;

	@Resource
	private SupplierApiMapper supplierApiMapper;

	@Resource
	private SupplierService supplierService;

	@Resource
	private ApiService apiService;

	public List<SupplierPO> findAllByApiCode(String apiCode) {

		ApiPO api = apiService.findByApiCode(apiCode);
		return supplierApiRepo.findAllByApi(api).stream().map(SupplierApiPO::getSupplier).toList();

	}

	public Long create(SupplierApiCreateDTO req) {

		SupplierPO supplier = supplierService.findById(req.getSupplier().getId());

		ApiPO api = apiService.findById(req.getApi().getId());

		Optional<SupplierApiPO> bySupplierAndApi = supplierApiRepo.findBySupplierAndApi(supplier, api);
		if (bySupplierAndApi.isPresent()) {
			throw SupplierException.duplicateApiBind();
		}

		SupplierApiPO po = supplierApiMapper.fromCreateDTO(req);
		return supplierApiRepo.save(po).getId();

	}

	public void update(SupplierApiUpdateDTO req) {

		SupplierPO supplier = supplierService.findById(req.getSupplier().getId());
		ApiPO api = apiService.findById(req.getApi().getId());
		Optional<SupplierApiPO> bySupplierAndApi = supplierApiRepo.findBySupplierAndApi(supplier, api);
		if (bySupplierAndApi.isPresent() && bySupplierAndApi.get().getId().equals(req.getSupplier().getId())) {
			throw SupplierException.duplicateApiBind();
		}

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