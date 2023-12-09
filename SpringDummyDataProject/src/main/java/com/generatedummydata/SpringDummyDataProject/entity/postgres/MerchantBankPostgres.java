package com.generatedummydata.SpringDummyDataProject.entity.postgres;

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
public class MerchantBankPostgres {

    @EmbeddedId
    private MerchantBankPostgresPrimaryKey merchantBankPostgresPrimaryKey;

    @MapsId("mer_id")
    @Column(name = "mer_id", insertable=false, updatable=false)
    @NonNull
    private String merchantId;

    @MapsId("ain_no")
    @Column(name = "ain_no", insertable=false, updatable=false)
    @NonNull
    private String ainNumber;

    @MapsId("bus_ctr_cd")
    @Column(name = "bus_ctr_cd", insertable=false, updatable=false)
    @NonNull
    private String busCtrCd;

    @MapsId("srce_sys_id")
    @Column(name = "srce_sys_id", insertable=false, updatable=false)
    @NonNull
    private String srcSysId;

    @MapsId("settle_acct_type_cd")
    @Column(name = "settle_acct_type_cd", insertable=false, updatable=false)
    @NonNull
    private String acctTypeCd;

    @Column(name = "cr_bank_acct_no")
    @NonNull
    private String crBankAcctNo;

    @Column(name = "cr_bank_acct_cd")
    @NonNull
    private String crBankAcctCd;

    @Column(name = "dr_bank_acct_no")
    @NonNull
    private String drBankAcctNo;

    @Column(name = "dr_bank_acct_cd")
    @NonNull
    private String drBankAcctCd;

    @Column(name = "trs_fsi_acct_no")
    @NonNull
    private String trsFsiAcctNo;

    @Column(name = "trs_fsi_refer_no")
    @NonNull
    private String trsFsiReferNo;

    @Column(name = "bank_acct_nm")
    @NonNull
    private String bankAcctNm;

    @Column(name = "strt_dt")
    @NonNull
    private String startDt;

    @Column(name = "end_dt")
    @NonNull
    private String endDt;

    @Column(name = "cr_bank_acct_swift_no")
    @NonNull
    private String crBankAcctSwiftNo;

    @Column(name = "dr_bank_acct_swift_no")
    @NonNull
    private String drBankAcctSwiftNo;

    @Column(name = "min_settle_am")
    @NonNull
    private Long minSettleAm;

    @Column(name = "agr_day_to_pay")
    @org.springframework.lang.NonNull
    private int agrDayToPay;

    @Column(name = "payee_nm")
    @NonNull
    private String payeeName;

    @Column(name = "rec_creat_ts")
    @NonNull
    private String recCreateTs;

    @Column(name = "lst_updt_ts")
    @NonNull
    private String lstUpdateTs;

}
