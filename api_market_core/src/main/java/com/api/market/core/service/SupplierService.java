package com.api.market.core.service;

import com.api.market.core.dto.supplier.*;
import com.api.market.core.exception.SupplierException;
import com.api.market.core.mapper.SupplierMapper;
import com.api.market.core.po.SupplierPO;
import com.api.market.core.repo.SupplierRepo;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.EasyExcel;
import javax.servlet.http.HttpServletResponse;

@Service
public class SupplierService {

    @Resource
    private SupplierRepo supplierRepo;

    @Resource
    private SupplierMapper supplierMapper;

    @Transactional(rollbackFor = Exception.class)
    public SupplierResDTO create(SupplierCreateReqDTO dto) {
        // 检查供应商编码是否已存在
        if (supplierRepo.findBySupplierCode(dto.getSupplierCode()).isPresent()) {
            throw SupplierException.supplierCodeExist();
        }

        SupplierPO supplier = supplierMapper.fromCreateDTO(dto);
        supplier = supplierRepo.save(supplier);
        return supplierMapper.toDTO(supplier);
    }

    @Transactional(rollbackFor = Exception.class)
    public SupplierResDTO update(SupplierUpdateReqDTO dto) {
        SupplierPO supplier = supplierRepo.findById(dto.getId())
                .orElseThrow(SupplierException::notFound);

        supplierMapper.updateFromDTO(supplier, dto);
        supplier = supplierRepo.save(supplier);
        return supplierMapper.toDTO(supplier);
    }

    public Page<SupplierResDTO> query(SupplierQueryReqDTO dto) {
        Specification<SupplierPO> spec = (root, query, cb) -> null;

        if (dto.getSupplierName() != null) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("supplierName"), "%" + dto.getSupplierName() + "%"));
        }
        if (dto.getSupplierCode() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("supplierCode"), dto.getSupplierCode()));
        }
        if (dto.getContactName() != null) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("contactName"), "%" + dto.getContactName() + "%"));
        }
        if (dto.getStatus() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), dto.getStatus()));
        }

        PageRequest pageRequest = PageRequest.of(
                dto.getPageNum() - 1,
                dto.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createTime"));

        return supplierRepo.findAll(spec, pageRequest)
                .map(supplierMapper::toDTO);
    }

    public Optional<SupplierPO> findById(String id) {
        return supplierRepo.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateStatus(List<String> ids, Integer status) {
        List<SupplierPO> suppliers = supplierRepo.findAllById(ids);
        suppliers.forEach(supplier -> supplier.setStatus(status));
        supplierRepo.saveAll(suppliers);
    }

    public void export(SupplierQueryReqDTO dto, HttpServletResponse response) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("供应商列表", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        // 查询数据
        List<SupplierResDTO> list = query(dto).getContent();

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