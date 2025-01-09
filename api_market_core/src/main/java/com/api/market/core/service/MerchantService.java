package com.api.market.core.service;

import cn.hutool.core.util.RandomUtil;
import com.api.market.core.dto.base.PageDTO;
import com.api.market.core.dto.merchant.MerchantCreateReqDTO;
import com.api.market.core.dto.merchant.MerchantQueryReqDTO;
import com.api.market.core.dto.merchant.MerchantResDTO;
import com.api.market.core.dto.merchant.MerchantUpdateReqDTO;
import com.api.market.core.exception.MerchantException;
import com.api.market.core.jpa.PkPageable;
import com.api.market.core.mapper.MerchantMapper;
import com.api.market.core.po.MerchantPO;
import com.api.market.core.repo.MerchantRepo;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MerchantService {

	@Resource
	private MerchantRepo merchantRepo;

	@Resource
	private MerchantMapper merchantMapper;

	@Transactional(rollbackFor = Exception.class)
	public Long create(MerchantCreateReqDTO dto) {
		// 检查商户编码是否已存在
		if (merchantRepo.existsByMerCode(dto.getMerCode())) {
			throw MerchantException.merCodeExist();
		}
		MerchantPO merchant = merchantMapper.fromCreateDTO(dto);

		merchant.setAppKey(generateAppKey());
		merchant.setAppSecret(generateAppSecret());

		merchantRepo.save(merchant);
		return merchant.getId();
	}

	@Transactional(rollbackFor = Exception.class)
	public void update(MerchantUpdateReqDTO dto) {
		MerchantPO merchant = merchantRepo.findById(dto.getId())
				.orElseThrow(MerchantException::notFound);

		merchantMapper.fromUpdateDTO(merchant, dto);
		merchantRepo.save(merchant);
	}

	public MerchantResDTO get(Long id) {
		return merchantMapper.toDto(findById(id));
	}

	public PageDTO<MerchantResDTO> search(MerchantQueryReqDTO dto) {
		Page<MerchantPO> pages = merchantRepo.search(dto, PkPageable.ofDefaultSort(dto.getPage(), dto.getSize()));
		return PageDTO.from(pages, po -> merchantMapper.toDto(po));
	}

	public MerchantPO findById(Long id) {
		return merchantRepo.findById(id).orElseThrow(MerchantException::notFound);
	}

	private String generateAppKey() {
		return RandomUtil.randomString(16);
	}

	private String generateAppSecret() {
		return RandomUtil.randomString(16);
	}
}