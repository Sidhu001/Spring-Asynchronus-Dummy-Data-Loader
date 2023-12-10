package com.generatedummydata.SpringDummyDataProject.service;

import com.generatedummydata.SpringDummyDataProject.entity.mysql.Merchant;
import com.generatedummydata.SpringDummyDataProject.entity.postgres.MerchantBankPostgres;
import com.generatedummydata.SpringDummyDataProject.repository.postgresrepo.MerchantPostgresRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.collections4.ListUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;


@Service("merchantServicePostgresImpl")
public class MerchantServicePostgresImpl implements MerchantServicePostgres {

    public static final Logger logger = LogManager.getLogger(MerchantServicePostgresImpl.class);
    @Autowired
    MerchantPostgresRepository merchantPostgresRepository;
    @Autowired
    MerchantService merchantService;

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private JdbcTemplate postgresJdbcTemplate;

    @Override
    public String saveMerchant() throws Exception {
        List<MerchantBankPostgres> merchantToBeSaved = new ArrayList<>();

        String query = "insert into merchant_bank (agr_day_to_pay,bank_acct_nm,cr_bank_acct_cd,cr_bank_acct_no,cr_bank_acct_swift_no,dr_bank_acct_cd,dr_bank_acct_no," + "dr_bank_acct_swift_no,end_dt,lst_updt_ts,min_settle_am,payee_nm,rec_creat_ts,strt_dt,trs_fsi_acct_no,trs_fsi_refer_no,ain_no,bus_ctr_cd,mer_id," + "settle_acct_type_cd,srce_sys_id) values (?,?,?,?,?,?,?,?,?,?,to_timestamp(?, 'YYYY-MM-DD'),?,?,to_timestamp(?, 'YYYY-MM-DD'),?,?,?,?,?,?,?)";

        String queryWithParams = "insert into merchant_bank (agr_day_to_pay,bank_acct_nm,cr_bank_acct_cd,cr_bank_acct_no,cr_bank_acct_swift_no," + "dr_bank_acct_cd,dr_bank_acct_no,dr_bank_acct_swift_no,end_dt,lst_updt_ts,min_settle_am,payee_nm,rec_creat_ts,strt_dt," + "trs_fsi_acct_no,trs_fsi_refer_no,ain_no,bus_ctr_cd,mer_id,settle_acct_type_cd,srce_sys_id) values (31,'1f83e0768c724a7eb941cfe86adc1c'," + "'7a9d57e','628234854436411','29425591382','150293f','784815598755657','18380565290','2001-12-16',to_timestamp('2012-08-16 01:15:57.489000','YYYY-MM-DD')," + "384560533602304,'d58339ab88594742b1956abe4',to_timestamp('2012-01-15 03:19:45.730000','YYYY-MM-DD') ,'2010-01-04','" + "7880963137','2266585759','11111111111','016','8467898439','002','F25')";
        postgresJdbcTemplate.execute(queryWithParams);
        return "Saved";
    }

    @Override
    public List<Merchant> fetchMerchants(String ssid) throws Exception {
        CompletableFuture<List<Merchant>> merchants = CompletableFuture.supplyAsync(() -> {
            try {
                return merchantService.findAllMerchantWithSsid(ssid);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).get();
        return merchants.get();
    }

    @Override
    public Map<String, Integer> saveAllMerchantsToDb(String ssId, int chunkSize, int batchUpsertSize, int noOfParallelBatchInserts) throws Exception {
        Map<String, Integer> jobSummary = new HashMap<>();
        List<Merchant> merchants = fetchMerchants(ssId);
        logger.info("Fetched all the merchants size: {}", merchants.size());
        String query = "insert into merchant_bank (agr_day_to_pay,bank_acct_nm,cr_bank_acct_cd,cr_bank_acct_no,cr_bank_acct_swift_no,dr_bank_acct_cd,dr_bank_acct_no,dr_bank_acct_swift_no,end_dt,lst_updt_ts,min_settle_am,payee_nm,rec_creat_ts,strt_dt,trs_fsi_acct_no,trs_fsi_refer_no,ain_no,bus_ctr_cd,mer_id,settle_acct_type_cd,srce_sys_id) values (?,?,?,?,?,?,?,?,TO_DATE(?,'YYYYMMDD'),to_timestamp(?,'YYYY-MM-DD'),?,?,to_timestamp(?,'YYYY-MM-DD'),TO_DATE(?,'YYYYMMDD'),?,?,?,?,?,?,?)";

        logger.info("Converting all merchants to list of chunks");
        List<List<Merchant>> merchantPartitions = ListUtils.partition(merchants, chunkSize);
        long dbProcessStartTime = System.currentTimeMillis();
        AtomicInteger chunkNo = new AtomicInteger();
        logger.info("Data insert process has started");
        merchantPartitions.parallelStream().forEach(merchantChunk -> {
            try {
                postgresJdbcTemplate.batchUpdate(
                        query,
                        merchantChunk,
                        batchUpsertSize,
                        (PreparedStatement ps, Merchant merchant) -> {
                            ps.setInt(1, merchant.getAgrDayToPay());
                            ps.setString(2, merchant.getBankAcctNm());
                            ps.setString(3, merchant.getCrBankAcctCd());
                            ps.setString(4, merchant.getCrBankAcctNo());
                            ps.setString(5, merchant.getCrBankAcctSwiftNo());
                            ps.setString(6, merchant.getDrBankAcctCd());
                            ps.setString(7, merchant.getDrBankAcctNo());
                            ps.setString(8, merchant.getDrBankAcctSwiftNo());
                            ps.setString(9, merchant.getEndDt().replaceAll("-", ""));
                            ps.setString(10, merchant.getLstUpdateTs());
                            ps.setLong(11, merchant.getMinSettleAm());
                            ps.setString(12, merchant.getPayeeName());
                            ps.setString(13, merchant.getRecCreateTs());
                            ps.setString(14, merchant.getStartDt().replaceAll("-", ""));
                            ps.setString(15, merchant.getTrsFsiAcctNo());
                            ps.setString(16, merchant.getTrsFsiReferNo());
                            ps.setString(17, "11111111111");
                            ps.setString(18, merchant.getBusCtrCd());
                            ps.setString(19, merchant.getSeNumber());
                            ps.setString(20, merchant.getAcctTypeCd());
                            ps.setString(21, merchant.getSrcSysId());
                        });
            } catch (Exception ex) {
                logger.error("Possible constraint error");
            }
            jobSummary.put("Chunk No " + chunkNo, merchantChunk.size());
            chunkNo.incrementAndGet();
        });
        long dbProcessEndTime = System.currentTimeMillis();
        logger.info("Data insert process has completed time taken: {}", dbProcessEndTime - dbProcessStartTime);
        return jobSummary;
    }
}
