package com.api.market.core.service;

import com.api.market.core.dto.BatchEnableLongIdReqDTO;
import com.api.market.core.dto.api.ApiCreateReqDTO;
import com.api.market.core.dto.api.ApiQueryReqDTO;
import com.api.market.core.dto.api.ApiResDTO;
import com.api.market.core.dto.api.ApiUpdateReqDTO;
import com.api.market.core.dto.base.PageDTO;
import com.api.market.core.enums.ApiCode;
import com.api.market.core.exception.ApiCategoryException;
import com.api.market.core.exception.ApiException;
import com.api.market.core.jpa.PkPageable;
import com.api.market.core.mapper.ApiMapper;
import com.api.market.core.po.ApiPO;
import com.api.market.core.repo.ApiRepo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ApiService {

	@Resource
	private ApiRepo apiRepo;

	@Resource
	private ApiMapper apiMapper;

	@Resource
	private ApiCategoryService apiCategoryService;

	public Long create(ApiCreateReqDTO dto) {
		// 检查接口编码是否已存在
		if (apiRepo.existsByApiCode(dto.getApiCode())) {
			throw ApiException.apiCodeExist();
		}

		// 检查分类是否存在
		if (!apiCategoryService.existsById(dto.getCategory().getId())) {
			throw ApiCategoryException.notFound();
		}

		ApiPO api = apiMapper.fromCreateDTO(dto);
		apiRepo.save(api);
		return api.getId();
	}

	@Transactional(rollbackFor = Exception.class)
	public void update(ApiUpdateReqDTO dto) {
		ApiPO api = findById(dto.getId());
		if (existsByApiCode(dto.getApiCode()) && !Objects.equals(api.getId(), dto.getId())) {
			throw ApiException.apiCodeExist();
		}
		// 如果更新了分类，检查分类是否存在
		if (dto.getCategory().getId() != null) {
			if (!apiCategoryService.existsById(dto.getCategory().getId())) {
				throw ApiCategoryException.notFound();
			}
		}
		apiMapper.fromUpdateDTO(api, dto);
		apiRepo.save(api);
	}

	public ApiResDTO get(Long id) {
		return apiMapper.toDto(findById(id));
	}

	public boolean existsByApiCode(ApiCode apiCode) {
		return apiRepo.existsByApiCode(apiCode);
	}

	public List<ApiResDTO> list() {
		return apiRepo.findAll().stream().map(po -> apiMapper.toDto(po)).toList();
	}

	public PageDTO<ApiResDTO> search(ApiQueryReqDTO dto) {

		Page<ApiPO> pages = apiRepo.search(dto.getSearch(), dto.getApiCode(), dto.getEnable(), PkPageable.ofDefaultSort(dto.getPage(), dto.getSize()));
		return PageDTO.from(pages, po -> apiMapper.toDto(po));
	}

	public ApiPO findById(Long id) {
		return apiRepo.findById(id).orElseThrow(ApiException::notFound);
	}

	public ApiPO findByApiCode(ApiCode apiCode) {
		return apiRepo.findByApiCode(apiCode).orElseThrow(ApiException::notFound);
	}

	@Transactional(rollbackFor = Exception.class)
	public void batchUpdateStatus(BatchEnableLongIdReqDTO dto) {
		List<ApiPO> apis = apiRepo.findAllById(dto.getIds());
		apis.forEach(api -> api.setEnable(dto.getEnable()));
		apiRepo.saveAll(apis);
	}


}