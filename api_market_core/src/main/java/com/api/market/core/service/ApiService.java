package com.api.market.core.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.api.market.core.dto.api.ApiCreateReqDTO;
import com.api.market.core.dto.api.ApiQueryReqDTO;
import com.api.market.core.dto.api.ApiResDTO;
import com.api.market.core.dto.api.ApiUpdateReqDTO;
import com.api.market.core.exception.ApiException;
import com.api.market.core.mapper.ApiMapper;
import com.api.market.core.po.ApiPO;
import com.api.market.core.repo.ApiRepo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ApiService {

	@Resource
	private ApiRepo apiRepo;

	@Resource
	private ApiMapper apiMapper;

	@Resource
	private ApiCategoryService apiCategoryService;

	public ApiResDTO create(ApiCreateReqDTO dto) {
		// 检查接口编码是否已存在
		if (apiRepo.findByApiCode(dto.getApiCode()).isPresent()) {
			throw ApiException.apiCodeExist();
		}

		// 检查分类是否存在
		if (!apiCategoryService.existsById(dto.getCategoryId())) {
			throw ApiException.categoryNotFound();
		}

		ApiPO api = apiMapper.fromCreateDTO(dto);
		api = apiRepo.save(api);

		return getApiWithCategory(api);
	}

	@Transactional(rollbackFor = Exception.class)
	public ApiResDTO update(ApiUpdateReqDTO dto) {
		ApiPO api = apiRepo.findById(dto.getId())
				.orElseThrow(ApiException::notFound);

		// 如果更新了分类，检查分类是否存在
		if (dto.getCategoryId() != null && !dto.getCategoryId().equals(api.getCategoryId())) {
			if (!apiCategoryService.existsById(dto.getCategoryId())) {
				throw ApiException.categoryNotFound();
			}
		}

		apiMapper.updateFromDTO(api, dto);
		api = apiRepo.save(api);

		return getApiWithCategory(api);
	}

	public Page<ApiResDTO> query(ApiQueryReqDTO dto) {
		Specification<ApiPO> spec = (root, query, cb) -> null;

		if (dto.getApiCode() != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("apiCode"), dto.getApiCode()));
		}
		if (dto.getApiName() != null) {
			spec = spec.and((root, query, cb) -> cb.like(root.get("apiName"), "%" + dto.getApiName() + "%"));
		}
		if (dto.getCategoryId() != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("categoryId"), dto.getCategoryId()));
		}
		if (dto.getStatus() != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), dto.getStatus()));
		}

		PageRequest pageRequest = PageRequest.of(
				dto.getPageNum() - 1,
				dto.getPageSize(),
				Sort.by(Sort.Direction.DESC, "createTime"));

		return apiRepo.findAll(spec, pageRequest)
				.map(this::getApiWithCategory);
	}

	public Optional<ApiPO> findById(String id) {
		return apiRepo.findById(id);
	}

	@Transactional(rollbackFor = Exception.class)
	public void batchUpdateStatus(List<String> ids, Integer status) {
		List<ApiPO> apis = apiRepo.findAllById(ids);
		apis.forEach(api -> api.setStatus(status));
		apiRepo.saveAll(apis);
	}

	public void export(ApiQueryReqDTO dto, HttpServletResponse response) throws IOException {
		// 设置响应头
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding("utf-8");
		String fileName = URLEncoder.encode("API列表", "UTF-8");
		response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

		// 查询数据
		List<ApiResDTO> list = query(dto).getContent();

		// 创建Excel
		EasyExcel.write(response.getOutputStream(), ApiResDTO.class)
				.sheet("API列表")
				.doWrite(list);
	}

	@Transactional(rollbackFor = Exception.class)
	public void importData(MultipartFile file) throws IOException {
		EasyExcel.read(file.getInputStream(), ApiCreateReqDTO.class,
				new AnalysisEventListener<ApiCreateReqDTO>() {
					private final List<ApiCreateReqDTO> list = new ArrayList<>();

					@Override
					public void invoke(ApiCreateReqDTO data, AnalysisContext context) {
						list.add(data);
						if (list.size() >= 100) {
							saveData();
							list.clear();
						}
					}

					@Override
					public void doAfterAllAnalysed(AnalysisContext context) {
						saveData();
					}

					private void saveData() {
						list.forEach(dto -> {
							try {
								create(dto);
							} catch (Exception e) {
								log.error("Import API failed: {}", dto, e);
							}
						});
					}
				}).sheet().doRead();
	}

	public List<ApiResDTO> listByCategoryId(String categoryId) {
		return apiRepo.findByCategoryIdAndStatus(categoryId, 1)
				.stream()
				.map(this::getApiWithCategory)
				.toList();
	}

	private ApiResDTO getApiWithCategory(ApiPO api) {
		ApiResDTO dto = apiMapper.toDTO(api);
		apiCategoryService.findById(api.getCategoryId())
				.ifPresent(category -> dto.setCategoryName(category.getCategoryName()));
		return dto;
	}

	public void validateApi(String apiId) {
		ApiPO api = apiRepo.findById(apiId)
				.orElseThrow(ApiException::notFound);

		if (api.getStatus() != 1) {
			throw ApiException.apiDisabled();
		}
	}
}