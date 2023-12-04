package com.generatedummydata.SpringDummyDataProject.service;

import static com.generatedummydata.SpringDummyDataProject.constants.TableConstants.*;
import static com.generatedummydata.SpringDummyDataProject.utils.RandomDataGenerator.*;
import static com.generatedummydata.SpringDummyDataProject.utils.RandomDataGenerator.generateRandomStringOfRequiredLength;

import com.generatedummydata.SpringDummyDataProject.entity.Merchant;
import com.generatedummydata.SpringDummyDataProject.entity.MerchantPrimaryKey;
import com.generatedummydata.SpringDummyDataProject.repository.MerchantRepository;

import com.google.gson.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service(value = "merchantServiceImpl")
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    public static final Logger logger = LogManager.getLogger(MerchantServiceImpl.class);

    @Override
    @Async
    public CompletableFuture<Map<String, Integer>> saveDummyMerchantDataInBatches(int chunkSize, int batchSize, int noOfDummyDataRequired) throws Exception {
        AtomicInteger noOfDataPersisted = new AtomicInteger();
        Map<String, Integer> jobSummary = new HashMap<>();
        int noOfEventsNeeded = noOfDummyDataRequired / (chunkSize * batchSize);
        List<CompletableFuture<List<Merchant>>> generateDummyDataEventFutures = new ArrayList<>();

        for (int i = 0; i < noOfEventsNeeded; i++) {
            CompletableFuture<List<Merchant>>  eventFuture = generateDummyMerchantChunk(batchSize, chunkSize);
            generateDummyDataEventFutures.add(eventFuture);
        }

        logger.info("Dummy data generation event futures list {}", generateDummyDataEventFutures.size());

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

        // Job working on single thread fix
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
