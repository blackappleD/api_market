package com.api.market.core.service;

import com.api.market.core.dto.base.PageDTO;
import com.api.market.core.dto.category.ApiCategoryCreateReqDTO;
import com.api.market.core.dto.category.ApiCategoryQueryReqDTO;
import com.api.market.core.dto.category.ApiCategoryResDTO;
import com.api.market.core.dto.category.ApiCategoryUpdateReqDTO;
import com.api.market.core.exception.ApiCategoryException;
import com.api.market.core.jpa.PkPageable;
import com.api.market.core.mapper.ApiCategoryMapper;
import com.api.market.core.po.ApiCategoryPO;
import com.api.market.core.po.base.BasePO;
import com.api.market.core.repo.ApiCategoryRepo;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApiCategoryService {

	@Resource
	private ApiCategoryRepo apiCategoryRepo;

	@Resource
	private ApiCategoryMapper apiCategoryMapper;

	@Transactional(rollbackFor = Exception.class)
	public Long create(ApiCategoryCreateReqDTO dto) {
		ApiCategoryPO category = apiCategoryMapper.fromCreateDTO(dto);
		apiCategoryRepo.save(category);
		return category.getId();
	}

	@Transactional(rollbackFor = Exception.class)
	public void update(ApiCategoryUpdateReqDTO dto) {
		ApiCategoryPO category = apiCategoryRepo.findById(dto.getId())
				.orElseThrow(ApiCategoryException::notFound);
		apiCategoryMapper.fromUpdateDTO(category, dto);
		apiCategoryRepo.save(category);
	}

	public ApiCategoryResDTO get(Long id) {
		return apiCategoryMapper.toDto(findById(id));
	}

	public ApiCategoryPO findById(Long id) {
		return apiCategoryRepo.findById(id).orElseThrow(ApiCategoryException::notFound);
	}

	public PageDTO<ApiCategoryResDTO> page(ApiCategoryQueryReqDTO dto) {
		Page<ApiCategoryPO> pages = apiCategoryRepo.search(dto.getSearch(), dto.getEnable(), PkPageable.ofDefaultSort(dto.getPage(), dto.getSize()));
		return PageDTO.from(pages, po -> apiCategoryMapper.toDto(po));
	}

	public List<ApiCategoryResDTO> listAll() {
		return apiCategoryMapper.toDtoList(
				apiCategoryRepo.findAll(Sort.by(Sort.Direction.DESC, BasePO.CommonPO.Fields.createTime)));
	}

	public boolean existsById(Long id) {
		return apiCategoryRepo.existsById(id);
	}
}