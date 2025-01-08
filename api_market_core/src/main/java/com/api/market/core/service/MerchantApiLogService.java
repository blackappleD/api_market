package com.api.market.core.service;

import com.api.market.core.dto.merchantlog.MerchantApiLogQueryReqDTO;
import com.api.market.core.dto.merchantlog.MerchantApiLogResDTO;
import com.api.market.core.exception.MerchantException;
import com.api.market.core.po.MerchantApiLogPO;
import com.api.market.core.po.MerchantPO;
import com.api.market.core.repo.MerchantApiLogRepo;
import com.api.market.core.mapper.MerchantApiLogMapper;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class MerchantApiLogService {

	@Resource
	private MerchantApiLogRepo merchantApiLogRepo;

	@Resource
	private MerchantService merchantService;

	@Resource
	private ApiService apiService;

	@Resource
	private MerchantApiLogMapper merchantApiLogMapper;

	public Page<MerchantApiLogResDTO> query(MerchantApiLogQueryReqDTO dto) {
		Specification<MerchantApiLogPO> spec = (root, query, cb) -> null;

		if (dto.getMerchantId() != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("merchantId"), dto.getMerchantId()));
		}
		if (dto.getApiId() != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("apiId"), dto.getApiId()));
		}
		if (dto.getStatus() != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), dto.getStatus()));
		}
		if (dto.getStartTime() != null) {
			spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("requestTime"), dto.getStartTime()));
		}
		if (dto.getEndTime() != null) {
			spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("requestTime"), dto.getEndTime()));
		}

		PageRequest pageRequest = PageRequest.of(
				dto.getPageNum() - 1,
				dto.getPageSize(),
				Sort.by(Sort.Direction.DESC, "requestTime"));

		Page<MerchantApiLogPO> page = merchantApiLogRepo.findAll(spec, pageRequest);

		return page.map(po -> {
			MerchantApiLogResDTO logDTO = merchantApiLogMapper.toDTO(po);

			// 获取商户名称
			try {
				MerchantPO merchant = merchantService.findById(po.getMerchantId());
				logDTO.setMerchantName(merchant.getMerchantName());
			} catch (MerchantException ignore) {
			}
			// 获取API名称
			apiService.findById(po.getApiId())
					.ifPresent(api -> logDTO.setApiName(api.getApiName()));

			return logDTO;
		});
	}
}