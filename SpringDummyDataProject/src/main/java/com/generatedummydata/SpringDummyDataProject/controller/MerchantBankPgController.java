package com.generatedummydata.SpringDummyDataProject.controller;

import com.generatedummydata.SpringDummyDataProject.entity.postgres.MerchantBankPostgres;
import com.generatedummydata.SpringDummyDataProject.service.MerchantServiceImpl;
import com.generatedummydata.SpringDummyDataProject.service.MerchantServicePostgres;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/merchantpg")
public class MerchantBankPgController {

    public static final Logger logger = LogManager.getLogger(MerchantBankPgController.class);
    @Autowired
    MerchantServicePostgres merchantServicePostgres;

    @PostMapping(value = "/saveMerchant", produces = "application/json")
    public ResponseEntity<String> saveMerchant() throws Exception{
        String savedMerchant = merchantServicePostgres.saveMerchant();
        return new ResponseEntity<>(savedMerchant, HttpStatus.OK);
    }

    @PostMapping(value = "/loadMerchants/{ssId}/{chunkSize}/{batchUpsertSize}/{noOfParallelBatchInserts}", produces = "application/json")
    public ResponseEntity<Map<String, Integer>> loadMerchantsToDb(@PathVariable String ssId,
                                                                  @PathVariable int chunkSize,
                                                                  @PathVariable int batchUpsertSize,
                                                                  @PathVariable int noOfParallelBatchInserts) throws Exception {
        long start = System.currentTimeMillis();
        logger.info("Batch started");
        Map<String, Integer> jobSummary = merchantServicePostgres.saveAllMerchantsToDb(ssId,chunkSize,batchUpsertSize,noOfParallelBatchInserts);
        logger.info("Batch Ended");
        long end = System.currentTimeMillis();
        logger.info("Total time taken to complete the batch {}", end - start);
        return new ResponseEntity<>(jobSummary, HttpStatus.OK);
    }
}
