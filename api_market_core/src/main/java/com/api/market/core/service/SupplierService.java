package com.api.market.core.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
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
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

		supplierMapper.fromUpdateDTO(supplier, dto);
		supplierRepo.save(supplier);
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

	public void export(SupplierQueryReqDTO dto, HttpServletResponse response) throws IOException {
		// 设置响应头
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding("utf-8");
		String fileName = URLEncoder.encode("供应商列表", StandardCharsets.UTF_8);
		response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

		// 查询数据
		List<SupplierResDTO> list = new ArrayList<>(search(dto).getRecords());

		// 创建Excel
		EasyExcel.write(response.getOutputStream(), SupplierResDTO.class)
				.sheet("供应商列表")
				.doWrite(list);
	}

	@Transactional(rollbackFor = Exception.class)
	public void importData(MultipartFile file) throws IOException {
		EasyExcel.read(file.getInputStream(), SupplierCreateReqDTO.class,
				new AnalysisEventListener<SupplierCreateReqDTO>() {
					private final List<SupplierCreateReqDTO> list = new ArrayList<>();

					@Override
					public void invoke(SupplierCreateReqDTO data, AnalysisContext context) {
						list.add(data);
						// 达到BATCH_COUNT，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
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
								// 记录导入失败的数据
								log.error("Import supplier failed: {}", dto, e);
							}
						});
					}
				}).sheet().doRead();
	}
}