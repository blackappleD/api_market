package com.api.market.core.service;

import com.api.market.core.dto.BatchEnableLongIdReqDTO;
import com.api.market.core.dto.apisale.ApiSaleCreateReqDTO;
import com.api.market.core.dto.apisale.ApiSaleQueryReqDTO;
import com.api.market.core.dto.apisale.ApiSaleResDTO;
import com.api.market.core.dto.apisale.ApiSaleUpdateReqDTO;
import com.api.market.core.dto.base.PageDTO;
import com.api.market.core.exception.ApiException;
import com.api.market.core.exception.ApiSaleException;
import com.api.market.core.jpa.PkPageable;
import com.api.market.core.mapper.ApiSaleMapper;
import com.api.market.core.po.ApiPO;
import com.api.market.core.po.ApiSalePO;
import com.api.market.core.po.MerchantPO;
import com.api.market.core.repo.ApiSaleRepo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ApiSaleService {

	@Resource
	private ApiSaleRepo apiSaleRepo;

	@Resource
	private ApiSaleMapper apiSaleMapper;

	public Long create(ApiSaleCreateReqDTO dto) {

		if (apiSaleRepo.existsByMerchantIdAndApiId(dto.getMerchant().getId(), dto.getApi().getId())) {
			throw ApiSaleException.alreadyExist();
		}

		ApiSalePO po = apiSaleMapper.fromCreateDTO(dto);

		return apiSaleRepo.save(po).getId();

	}

	@Transactional(rollbackFor = Exception.class)
	public void update(ApiSaleUpdateReqDTO dto) {

		ApiSalePO po = findById(dto.getId());
		if (apiSaleRepo.existsByMerchantIdAndApiId(dto.getMerchant().getId(), dto.getApi().getId())
				&& !dto.getId().equals(po.getId())) {
			throw ApiSaleException.alreadyExist();
		}

		apiSaleMapper.fromUpdateDTO(po, dto);

	}

	public ApiSaleResDTO get(Long id) {

		ApiSalePO po = findById(id);
		return apiSaleMapper.toDto(po);

	}

	public List<ApiSaleResDTO> list() {
		return apiSaleRepo.findAll().stream().map(po -> apiSaleMapper.toDto(po)).toList();
	}

	public PageDTO<ApiSaleResDTO> search(ApiSaleQueryReqDTO dto) {

		Page<ApiSalePO> pages = apiSaleRepo.search(dto.getSearch(), dto.getEnable(), PkPageable.ofDefaultSort(dto.getPage(), dto.getSize()));
		return PageDTO.from(pages, po -> apiSaleMapper.toDto(po));
	}

	public ApiSalePO findById(Long id) {
		return apiSaleRepo.findById(id).orElseThrow(ApiException::notFound);
	}

	@Transactional(rollbackFor = Exception.class)
	public void batchUpdateStatus(BatchEnableLongIdReqDTO dto) {
		List<ApiSalePO> apis = apiSaleRepo.findAllById(dto.getIds());
		apis.forEach(api -> api.setEnable(dto.getEnable()));
		apiSaleRepo.saveAll(apis);
	}

	public ApiSalePO findBYMerchantAndApi(MerchantPO merchant, ApiPO api) {

		return apiSaleRepo.findByMerchantAndApi(merchant, api).orElseThrow(ApiSaleException::notFound);
	}


}