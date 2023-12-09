package com.generatedummydata.SpringDummyDataProject.entity.mysql;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "merchant_bank")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Merchant {

    @EmbeddedId
    private MerchantPrimaryKey merchantPrimaryKey;

    @MapsId("SE_NO")
    @Column(name = "SE_NO", insertable=false, updatable=false)
    @NonNull
    private String seNumber;

    @MapsId("SRCE_SYS_ID")
    @Column(name = "SRCE_SYS_ID", insertable=false, updatable=false)
    @NonNull
    private String srcSysId;

    @MapsId("BUS_CTR_CD")
    @Column(name = "BUS_CTR_CD", insertable=false, updatable=false)
    @NonNull
    private String busCtrCd;

    @MapsId("ACCT_TYPE_CD")
    @Column(name = "ACCT_TYPE_CD", insertable=false, updatable=false)
    @NonNull
    private String acctTypeCd;

    @Column(name = "CR_BANK_ACCT_NO")
    @NonNull
    private String crBankAcctNo;

    @Column(name = "CR_BANK_ACCT_CD")
    @NonNull
    private String crBankAcctCd;

    @Column(name = "DR_BANK_ACCT_NO")
    @NonNull
    private String drBankAcctNo;

    @Column(name = "DR_BANK_ACCT_CD")
    @NonNull
    private String drBankAcctCd;

    @Column(name = "TRS_FSI_ACCT_NO")
    @NonNull
    private String trsFsiAcctNo;

    @Column(name = "TRS_FSI_REFER_NO")
    @NonNull
    private String trsFsiReferNo;

    @Column(name = "BANK_ACCT_NM")
    @NonNull
    private String bankAcctNm;

    @Column(name = "STRT_DT")
    @NonNull
    private String startDt;

    @Column(name = "END_DT")
    @NonNull
    private String endDt;

    @Column(name = "CR_BANK_ACCT_SWIFT_NO")
    @NonNull
    private String crBankAcctSwiftNo;

    @Column(name = "DR_BANK_ACCT_SWIFT_NO")
    @NonNull
    private String drBankAcctSwiftNo;

    @Column(name = "MIN_SETTLE_AM")
    @NonNull
    private Long minSettleAm;

    @Column(name = "AGR_DAY_TO_PAY")
    @org.springframework.lang.NonNull
    private int agrDayToPay;

    @Column(name = "PAYEE_NM")
    @NonNull
    private String payeeName;

    @Column(name = "REC_CREAT_TS")
    @NonNull
    private String recCreateTs;

    @Column(name = "LST_UPDT_ODS_TS")
    @NonNull
    private String lstUpdateTs;

}
