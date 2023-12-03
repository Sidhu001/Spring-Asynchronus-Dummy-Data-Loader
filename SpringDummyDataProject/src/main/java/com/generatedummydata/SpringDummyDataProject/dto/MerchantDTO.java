package com.generatedummydata.SpringDummyDataProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@AllArgsConstructor
@Data
public class MerchantDTO {

    private String seNumber;

    private String srcSysId;

    private String busCtrCd;

    private String acctTypeCd;

    private String crBankAcctNo;

    private String crBankAcctCd;

    private String drBankAcctNo;

    private String drBankAcctCd;

    private String trsFsiAcctNo;

    private String trsFsiReferNo;

    private String bankAcctNm;

    private String startDt;

    private String endDt;

    private String crBankAcctSwiftNo;

    private String drBankAcctSwiftNo;

    private Long minSettleAm;

    private int agrDayToPay;

    private String payeeName;

    private String recCreateTs;

    private String lstUpdateTs;

}
