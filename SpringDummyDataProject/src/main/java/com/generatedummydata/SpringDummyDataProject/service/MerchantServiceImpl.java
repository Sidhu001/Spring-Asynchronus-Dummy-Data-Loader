package com.generatedummydata.SpringDummyDataProject.service;

import static com.generatedummydata.SpringDummyDataProject.constants.TableConstants.*;
import static com.generatedummydata.SpringDummyDataProject.utils.RandomDataGenerator.*;
import static com.generatedummydata.SpringDummyDataProject.utils.RandomDataGenerator.generateRandomStringOfRequiredLength;

import com.generatedummydata.SpringDummyDataProject.entity.mysql.*;

import com.generatedummydata.SpringDummyDataProject.repository.sqlrepo.MerchantRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service(value = "merchantServiceImpl")
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private JdbcTemplate mysqlJdbctemplate;
    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private JdbcTemplate postgresJdbcTemplate;
    public static final Logger logger = LogManager.getLogger(MerchantServiceImpl.class);

    @Override
    @Async
    public CompletableFuture<Map<String, Integer>> saveDummyMerchantDataInBatches(int chunkSize, int batchSize, int noOfDummyDataRequired) throws Exception {
        Map<String, Integer> jobSummary = new HashMap<>();
        int noOfEventsNeeded = noOfDummyDataRequired / (chunkSize * batchSize);
        List<CompletableFuture<List<Merchant>>> generateDummyDataEventFutures = new ArrayList<>();

        long dummyDataGenerationStart = System.currentTimeMillis();
        for (int i = 0; i < noOfEventsNeeded; i++) {
            CompletableFuture<List<Merchant>>  eventFuture = generateDummyMerchantChunk(batchSize, chunkSize);
            generateDummyDataEventFutures.add(eventFuture);
        }
        long dummyDataGenerationEnd = System.currentTimeMillis();

        logger.info("Dummy data generation event futures list {} ", generateDummyDataEventFutures.size());
        logger.info("Total time taken to generate dummy data {}", dummyDataGenerationEnd - dummyDataGenerationStart);

        /*      Below causes the job tu run in single thread only So below code is not correct
        AtomicInteger i = new AtomicInteger();
        for(CompletableFuture<List<Merchant>> event : generateDummyDataEventFutures){
            if(event.get() != null){
                Integer noOfDataStored = merchantRepository.saveAll(event.get()).size();
                jobSummary.put("event " + i + " stored", noOfDataStored );
                noOfDataPersisted.getAndAdd(event.get().size());
                i.incrementAndGet();
            }
        }*/

        List<List<Merchant>> dummyMerchantListFromAllParallelBatches ;
        dummyMerchantListFromAllParallelBatches = generateDummyDataEventFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        List<Merchant> allDummyMerchantsTOBeStored = dummyMerchantListFromAllParallelBatches.parallelStream().flatMap(List::stream).toList();
        long startDbPersistence = System.currentTimeMillis();

        //@JDBC Batch query support
        String merchantBankInsertQuery = "insert into merchant_bank (agr_day_to_pay,bank_acct_nm,cr_bank_acct_cd,cr_bank_acct_no," +
                "cr_bank_acct_swift_no,dr_bank_acct_cd,dr_bank_acct_no,dr_bank_acct_swift_no,end_dt,lst_updt_ods_ts,min_settle_am,payee_nm,rec_creat_ts," +
                "strt_dt,trs_fsi_acct_no,trs_fsi_refer_no,acct_type_cd,bus_ctr_cd,se_no,srce_sys_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        //Non Asynchronous batch call
        /*jdbctemplate.batchUpdate(merchantBankInsertQuery,
                allDummyMerchantsTOBeStored,
                100,
                (PreparedStatement ps, Merchant merchant) -> {
                    ps.setInt(1, merchant.getAgrDayToPay());
                    ps.setString(2, merchant.getBankAcctNm());
                    ps.setString(3, merchant.getCrBankAcctCd());
                    ps.setString(4, merchant.getCrBankAcctNo());
                    ps.setString(5, merchant.getCrBankAcctSwiftNo());
                    ps.setString(6, merchant.getDrBankAcctCd());
                    ps.setString(7, merchant.getDrBankAcctNo());
                    ps.setString(8, merchant.getDrBankAcctSwiftNo());
                    ps.setString(9, merchant.getEndDt());
                    ps.setString(10, merchant.getLstUpdateTs());
                    ps.setLong(11, merchant.getMinSettleAm());
                    ps.setString(12, merchant.getPayeeName());
                    ps.setString(13, merchant.getRecCreateTs());
                    ps.setString(14, merchant.getStartDt());
                    ps.setString(15, merchant.getTrsFsiAcctNo());
                    ps.setString(16, merchant.getTrsFsiReferNo());
                    ps.setString(17, merchant.getAcctTypeCd());
                    ps.setString(18, merchant.getBusCtrCd());
                    ps.setString(19, merchant.getSeNumber());
                    ps.setString(20, merchant.getSrcSysId());
                });*/

        // Asynchronous call to jdbc batchUpdate method
        AtomicInteger noOfDataPersisted = new AtomicInteger();
        generateDummyDataEventFutures.parallelStream().forEach(
                dummyDataFuture ->
                {
                    try {
                        int noOfDataStored = dummyDataFuture.get().size();
                        mysqlJdbctemplate.batchUpdate(merchantBankInsertQuery,
                                dummyDataFuture.get(),
                                100,
                                (PreparedStatement ps, Merchant merchant) -> {
                                    ps.setInt(1, merchant.getAgrDayToPay());
                                    ps.setString(2, merchant.getBankAcctNm());
                                    ps.setString(3, merchant.getCrBankAcctCd());
                                    ps.setString(4, merchant.getCrBankAcctNo());
                                    ps.setString(5, merchant.getCrBankAcctSwiftNo());
                                    ps.setString(6, merchant.getDrBankAcctCd());
                                    ps.setString(7, merchant.getDrBankAcctNo());
                                    ps.setString(8, merchant.getDrBankAcctSwiftNo());
                                    ps.setString(9, merchant.getEndDt());
                                    ps.setString(10, merchant.getLstUpdateTs());
                                    ps.setLong(11, merchant.getMinSettleAm());
                                    ps.setString(12, merchant.getPayeeName());
                                    ps.setString(13, merchant.getRecCreateTs());
                                    ps.setString(14, merchant.getStartDt());
                                    ps.setString(15, merchant.getTrsFsiAcctNo());
                                    ps.setString(16, merchant.getTrsFsiReferNo());
                                    ps.setString(17, merchant.getAcctTypeCd());
                                    ps.setString(18, merchant.getBusCtrCd());
                                    ps.setString(19, merchant.getSeNumber());
                                    ps.setString(20, merchant.getSrcSysId());
                                });
                        noOfDataPersisted.getAndAdd(noOfDataStored);
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
        );


        int noOfDataStored = allDummyMerchantsTOBeStored.size();
        long endDbPersistence = System.currentTimeMillis();
        logger.info("Total time taken to store records {}, : {}", noOfDataStored, endDbPersistence - startDbPersistence);

        jobSummary.put("TOTAL_DATA_STORED", noOfDataStored);
        logger.info("Job Summary {}", jobSummary.toString());
        return CompletableFuture.completedFuture(jobSummary);

        // TODO: JPA saveALl() method not inserting in batches even after enabling it in property (Maybe because we are not using any
        //            identifier strategy) need to check later unable to find fix in online
        /*
        AtomicInteger i = new AtomicInteger();
        generateDummyDataEventFutures.parallelStream().forEach(
                dummyDataFuture ->
                {
                    try {
                        int noOfDataStored = merchantRepository.saveAll(dummyDataFuture.get()).size();
                        jobSummary.put("event " + i + " stored", noOfDataStored );
                        noOfDataPersisted.getAndAdd(noOfDataStored);
                        i.incrementAndGet();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        Integer totalDataPersisted = noOfDataPersisted.intValue();
        jobSummary.put("TOTAL_DATA_STORED", totalDataPersisted);
        logger.info("Job Summary {}", jobSummary.toString());
        return CompletableFuture.completedFuture(jobSummary);
    */

    }

    @Override
    public List<Merchant> saveMerchant(int noOfMerchant) throws Exception {
        long start = System.currentTimeMillis();
        CompletableFuture<List<Merchant>> merchant = CompletableFuture.supplyAsync(() ->
                generateMerchant(noOfMerchant)).thenApply((merchantList) -> {
            List<Merchant> merchants;
            try {
                merchants = merchantList.get();
                for(Merchant merch: merchants) {
                    merchantRepository.save(merch);
                    logger.info("Successfully added Merchant data in table: {} ", merch.toString());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            long end = System.currentTimeMillis();
            logger.info("Total Time taken for {} records in single record db processing at a time {}", noOfMerchant, end - start);
            return merchants;
        });
        return merchant.get();
    }

    @Async
    private CompletableFuture<List<Merchant>> generateDummyMerchantChunk(int parallelBatch, int chunkSize){
        List<List<Merchant>> dummyMerchantChunk;
        List<CompletableFuture<List<Merchant>>> dummyDataChunkFutures =  new ArrayList<>();
        for(int i=0; i<parallelBatch;i++) {
            CompletableFuture<List<Merchant>> dummyRecords = generateMerchant(chunkSize);
            dummyDataChunkFutures.add(dummyRecords);
        }
        dummyMerchantChunk = dummyDataChunkFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        List<Merchant> dummyMerchantChunkMerged = dummyMerchantChunk.stream().flatMap(List::stream).toList();
        return CompletableFuture.completedFuture(dummyMerchantChunkMerged);
    }

    @Override
    @Async
    public CompletableFuture<List<Merchant>> findAllMerchantWithSsid(String ssid) throws Exception {
        List<Merchant> merchants = CompletableFuture.supplyAsync(() -> {
            try {
                return merchantRepository.findBySrcSysId(ssid);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).get();
        return CompletableFuture.completedFuture(merchants);
    }

    @Async
    private CompletableFuture<List<Merchant>> generateMerchant(int chunkSize) {
        List<Merchant> dummyMerchantChunk = new ArrayList<>();
        for(int i=0; i<chunkSize; i++){
            Merchant merchant = new Merchant();
            merchant.setSeNumber(generateRandomNumber(10));
            merchant.setSrcSysId(generateRandomThreeCharacterStringsFromGivenArray(SRCE_SYS_ID));
            merchant.setBusCtrCd(generateRandomThreeCharacterStringsFromGivenArray(BUS_CTR_CD));
            merchant.setAcctTypeCd(generateRandomThreeCharacterStringsFromGivenArray(ACCT_TYPE_CD));
            merchant.setCrBankAcctNo(generateRandomNumber(15));
            merchant.setCrBankAcctCd(generateRandomStringOfRequiredLength(7));
            merchant.setDrBankAcctNo(generateRandomNumber(15));
            merchant.setDrBankAcctCd(generateRandomStringOfRequiredLength(7));
            merchant.setTrsFsiAcctNo(generateRandomNumber(10));
            merchant.setTrsFsiReferNo(generateRandomNumber(10));
            merchant.setBankAcctNm(generateRandomStringOfRequiredLength(30));
            merchant.setStartDt(generateRandomDate());
            merchant.setEndDt(generateRandomDate());
            merchant.setCrBankAcctSwiftNo(generateRandomNumber(11));
            merchant.setDrBankAcctSwiftNo(generateRandomNumber(11));
            merchant.setMinSettleAm(generateRandomLongValue());
            merchant.setAgrDayToPay(generateRandomInt(10, 60));
            merchant.setPayeeName(generateRandomStringOfRequiredLength(25));
            merchant.setRecCreateTs(generateRandomDateTimeStamp());
            merchant.setLstUpdateTs(generateRandomDateTimeStamp());
            merchant.setMerchantPrimaryKey(new MerchantPrimaryKey(merchant.getSeNumber(),
                    merchant.getSrcSysId(), merchant.getBusCtrCd(),
                    merchant.getAcctTypeCd()));
            dummyMerchantChunk.add(merchant);
        }
        return CompletableFuture.completedFuture(dummyMerchantChunk);
    }
}
