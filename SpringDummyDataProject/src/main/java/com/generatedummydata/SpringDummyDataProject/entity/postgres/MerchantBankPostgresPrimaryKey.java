package com.generatedummydata.SpringDummyDataProject.entity.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class MerchantBankPostgresPrimaryKey implements Serializable {

    @Column(name = "mer_id")
    @NonNull
    private String merchantId;

    @Column(name = "ain_no")
    @NonNull
    private String ainNumber;

    @Column(name = "bus_ctr_cd")
    @NonNull
    private String busCtrCd;

    @Column(name = "srce_sys_id")
    @NonNull
    private String srcSysId;

    @Column(name = "settle_acct_type_cd")
    @NonNull
    private String settleAcctTypeCd;
}
