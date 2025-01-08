package com.api.market.core.service;

import com.api.market.core.dto.category.*;
import com.api.market.core.exception.ApiException;
import com.api.market.core.mapper.ApiCategoryMapper;
import com.api.market.core.po.ApiCategoryPO;
import com.api.market.core.repo.ApiCategoryRepo;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ApiCategoryService {

    @Resource
    private ApiCategoryRepo apiCategoryRepo;

    @Resource
    private ApiCategoryMapper apiCategoryMapper;

    @Transactional(rollbackFor = Exception.class)
    public ApiCategoryResDTO create(ApiCategoryCreateReqDTO dto) {
        ApiCategoryPO category = apiCategoryMapper.fromCreateDTO(dto);
        category = apiCategoryRepo.save(category);
        return apiCategoryMapper.toDTO(category);
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiCategoryResDTO update(ApiCategoryUpdateReqDTO dto) {
        ApiCategoryPO category = apiCategoryRepo.findById(dto.getId())
                .orElseThrow(ApiException::categoryNotFound);

        apiCategoryMapper.updateFromDTO(category, dto);
        category = apiCategoryRepo.save(category);
        return apiCategoryMapper.toDTO(category);
    }

    public Page<ApiCategoryResDTO> query(ApiCategoryQueryReqDTO dto) {
        Specification<ApiCategoryPO> spec = (root, query, cb) -> null;

        if (dto.getCategoryName() != null) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("categoryName"), "%" + dto.getCategoryName() + "%"));
        }
        if (dto.getStatus() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), dto.getStatus()));
        }

        PageRequest pageRequest = PageRequest.of(
                dto.getPageNum() - 1,
                dto.getPageSize(),
                Sort.by(Sort.Direction.ASC, "sort", "createTime"));

        return apiCategoryRepo.findAll(spec, pageRequest)
                .map(apiCategoryMapper::toDTO);
    }

    public List<ApiCategoryResDTO> listAll() {
        return apiCategoryMapper.toDTOList(
                apiCategoryRepo.findAll(Sort.by(Sort.Direction.ASC, "sort", "createTime")));
    }

    public Optional<ApiCategoryPO> findById(String id) {
        return apiCategoryRepo.findById(id);
    }

    public boolean existsById(String id) {
        return apiCategoryRepo.existsById(id);
    }
}