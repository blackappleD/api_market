package com.api.market.core.service;

import com.api.market.core.dto.BatchEnableLongIdReqDTO;
import com.api.market.core.dto.base.PageDTO;
import com.api.market.core.dto.supplier.SupplierCreateReqDTO;
import com.api.market.core.dto.supplier.SupplierQueryReqDTO;
import com.api.market.core.dto.supplier.SupplierResDTO;
import com.api.market.core.dto.supplier.SupplierUpdateReqDTO;
import com.api.market.core.exception.SupplierException;
import com.api.market.core.jpa.PkPageable;
import com.api.market.core.mapper.SupplierMapper;
import com.api.market.core.po.SupplierPO;
import com.api.market.core.po.base.BasePO;
import com.api.market.core.repo.SupplierRepo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class SupplierService {

	@Resource
	private SupplierRepo supplierRepo;

	@Resource
	private SupplierMapper supplierMapper;

	@Transactional(rollbackFor = Exception.class)
	public Long create(SupplierCreateReqDTO dto) {
		// 检查供应商编码是否已存在
		if (supplierRepo.existsBySupCode(dto.getSupCode())) {
			throw SupplierException.supplierCodeExist();
		}

		SupplierPO supplier = supplierMapper.fromCreateDTO(dto);
		supplierRepo.save(supplier);
		return supplier.getId();
	}

	@Transactional(rollbackFor = Exception.class)
	public void update(SupplierUpdateReqDTO dto) {
		SupplierPO supplier = findById(dto.getId());
		if (existsBySupCode(dto.getSupCode()) && !supplier.getId().equals(dto.getId())) {
			throw SupplierException.supplierCodeExist();
		}

		supplierMapper.fromUpdateDTO(supplier, dto);
		supplierRepo.save(supplier);
	}

	public boolean existsBySupCode(String supCode) {
		return supplierRepo.existsBySupCode(supCode);
	}

	public List<SupplierResDTO> list() {
		return supplierRepo.findAll().stream().map(po -> supplierMapper.toDto(po)).toList();
	}

	public PageDTO<SupplierResDTO> search(SupplierQueryReqDTO dto) {
		Page<SupplierPO> pages = supplierRepo.search(dto, PkPageable.of(dto.getPage(), dto.getSize(),
				Sort.by(Sort.Direction.DESC, BasePO.CommonPO.Fields.createTime)));
		return PageDTO.from(pages, po -> supplierMapper.toDto(po));
	}

	public SupplierResDTO get(Long id) {
		return supplierMapper.toDto(findById(id));
	}

	public SupplierPO findById(Long id) {
		return supplierRepo.findById(id).orElseThrow(SupplierException::notFound);
	}

	@Transactional(rollbackFor = Exception.class)
	public void batchUpdateStatus(BatchEnableLongIdReqDTO dto) {
		List<SupplierPO> suppliers = supplierRepo.findAllById(dto.getIds());
		suppliers.forEach(supplier -> supplier.setEnable(dto.getEnable()));
		supplierRepo.saveAll(suppliers);
	}

}