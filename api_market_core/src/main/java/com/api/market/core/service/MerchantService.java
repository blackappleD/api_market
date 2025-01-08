package com.api.market.core.service;

import cn.hutool.core.util.IdUtil;
import com.api.market.core.dto.merchant.MerchantCreateReqDTO;
import com.api.market.core.dto.merchant.MerchantQueryReqDTO;
import com.api.market.core.dto.merchant.MerchantResDTO;
import com.api.market.core.dto.merchant.MerchantUpdateReqDTO;
import com.api.market.core.exception.MerchantException;
import com.api.market.core.mapper.MerchantMapper;
import com.api.market.core.po.MerchantPO;
import com.api.market.core.repo.MerchantRepo;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MerchantService {

	@Resource
	private MerchantRepo merchantRepo;

	@Resource
	private MerchantMapper merchantMapper;

	@Transactional(rollbackFor = Exception.class)
	public MerchantResDTO create(MerchantCreateReqDTO dto) {
		// 检查商户编码是否已存在
		if (merchantRepo.findByMerchantCode(dto.getMerchantCode()).isPresent()) {
			throw MerchantException.merCodeExist();
		}

		MerchantPO merchant = merchantMapper.fromCreateDTO(dto);

		// 生成appKey和appSecret
		merchant.setAppKey(generateAppKey());
		merchant.setAppSecret(generateAppSecret());

		merchant = merchantRepo.save(merchant);
		return merchantMapper.toDTO(merchant);
	}

	@Transactional(rollbackFor = Exception.class)
	public MerchantResDTO update(MerchantUpdateReqDTO dto) {
		MerchantPO merchant = merchantRepo.findById(dto.getId())
				.orElseThrow(MerchantException::notFound);

		merchantMapper.updateFromDTO(merchant, dto);
		merchant = merchantRepo.save(merchant);
		return merchantMapper.toDTO(merchant);
	}

	public Page<MerchantResDTO> query(MerchantQueryReqDTO dto) {
		Specification<MerchantPO> spec = (root, query, cb) -> null;

		// 构建查询条件
		if (dto.getMerchantName() != null) {
			spec = spec.and((root, query, cb) -> cb.like(root.get("merchantName"), "%" + dto.getMerchantName() + "%"));
		}
		if (dto.getMerchantCode() != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("merchantCode"), dto.getMerchantCode()));
		}
		if (dto.getStatus() != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), dto.getStatus()));
		}

		PageRequest pageRequest = PageRequest.of(
				dto.getPageNum() - 1,
				dto.getPageSize(),
				Sort.by(Sort.Direction.DESC, "createTime"));

		return merchantRepo.findAll(spec, pageRequest).map(merchantMapper::toDTO);
	}

	public MerchantPO findById(Long id) {
		return merchantRepo.findById(id).orElseThrow(MerchantException::notFound);
	}

	private String generateAppKey() {
		return IdUtil.fastSimpleUUID();
	}

	private String generateAppSecret() {
		return IdUtil.fastSimpleUUID();
	}
}