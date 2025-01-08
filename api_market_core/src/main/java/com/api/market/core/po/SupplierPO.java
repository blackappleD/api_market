package com.api.market.core.po;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = SupplierPO.TABLE_NAME)
public class SupplierPO extends BasePO.CommonPO<Long> {

    public static final String TABLE_NAME = "am_supplier";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "supplier_name", nullable = false, length = 100)
    private String supplierName;

    @Column(name = "supplier_code", nullable = false, length = 50, unique = true)
    private String supplierCode;

    @Column(name = "contact_name", length = 50)
    private String contactName;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

}