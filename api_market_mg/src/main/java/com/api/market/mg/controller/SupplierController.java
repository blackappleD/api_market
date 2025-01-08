package com.api.market.mg.controller;

import com.api.market.core.dto.supplier.*;
import com.api.market.core.service.SupplierService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Resource
    private SupplierService supplierService;

    @PostMapping
    public SupplierResDTO create(@Valid @RequestBody SupplierCreateReqDTO dto) {
        return supplierService.create(dto);
    }

    @PutMapping
    public SupplierResDTO update(@Valid @RequestBody SupplierUpdateReqDTO dto) {
        return supplierService.update(dto);
    }

    @GetMapping("/page")
    public Page<SupplierResDTO> page(@Valid SupplierQueryReqDTO dto) {
        return supplierService.query(dto);
    }

    @GetMapping("/{id}")
    public SupplierResDTO getById(@PathVariable String id) {
        return supplierService.findById(id)
                .map(supplierMapper::toDTO)
                .orElseThrow(SupplierException::notFound);
    }

    @PostMapping("/batch-enable")
    public void batchEnable(@RequestBody List<String> ids) {
        supplierService.batchUpdateStatus(ids, 1);
    }

    @PostMapping("/batch-disable")
    public void batchDisable(@RequestBody List<String> ids) {
        supplierService.batchUpdateStatus(ids, 0);
    }

    @GetMapping("/export")
    public void export(SupplierQueryReqDTO dto, HttpServletResponse response) throws IOException {
        supplierService.export(dto, response);
    }

    @PostMapping("/import")
    public void importData(MultipartFile file) throws IOException {
        supplierService.importData(file);
    }
}