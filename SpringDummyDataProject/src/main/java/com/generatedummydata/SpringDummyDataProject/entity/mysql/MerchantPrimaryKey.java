package com.generatedummydata.SpringDummyDataProject.entity.mysql;

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
public class MerchantPrimaryKey implements Serializable {

    @Column(name = "SE_NO")
    @NonNull
    private String seNumber;

    @Column(name = "SRCE_SYS_ID")
    @NonNull
    private String srcSysId;

    @Column(name = "BUS_CTR_CD")
    @NonNull
    private String busCtrCd;

    @Column(name = "ACCT_TYPE_CD")
    @NonNull
    private String acctTypeCd;
}
