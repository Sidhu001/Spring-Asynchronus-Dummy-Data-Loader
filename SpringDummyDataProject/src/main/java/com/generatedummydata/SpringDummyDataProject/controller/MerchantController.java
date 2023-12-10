package com.generatedummydata.SpringDummyDataProject.controller;

import com.generatedummydata.SpringDummyDataProject.entity.mysql.Merchant;
import com.generatedummydata.SpringDummyDataProject.service.MerchantService;
import com.generatedummydata.SpringDummyDataProject.service.MerchantServiceImpl;
import com.google.gson.JsonArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/merchant")
public class MerchantController {
    public static final Logger logger = LogManager.getLogger(MerchantServiceImpl.class);

    @Autowired
    MerchantService merchantService;

    //For synchronous Operation
    @PostMapping(value = "/saveMerchant/{noOfMerchants}", produces = "application/json")
    public ResponseEntity<List<Merchant>> saveMerchant(@PathVariable int noOfMerchants) throws Exception {
        List<Merchant> merchants = merchantService.saveMerchant(noOfMerchants);
        return new ResponseEntity<>(merchants, HttpStatus.OK);
    }

    //For storing data asynchronously
    @PostMapping(value = "/saveAllMerchants/{chunkSize}/{batchSize}/{noOfDummyDataRequired}", produces = "application/json")
    public ResponseEntity<Map<String, Integer>> saveAllMerchants(@PathVariable int chunkSize,
                                                       @PathVariable int batchSize,
                                                       @PathVariable int noOfDummyDataRequired) throws Exception {
        long start = System.currentTimeMillis();
        logger.info("Received request in asynchronous data loader controller" );
        Map<String, Integer> jobSummary = merchantService.saveDummyMerchantDataInBatches(chunkSize, batchSize, noOfDummyDataRequired).get();
        long end = System.currentTimeMillis();
        logger.info("Data loading process took {} for dummy data of {}", end-start, noOfDummyDataRequired);
        return new ResponseEntity<>(jobSummary, HttpStatus.OK);
    }

    //For getting all data belonging to a particular Ssid
    @GetMapping(value = "/getAllMerchants/{ssid}" , produces = "application/json")
    public ResponseEntity<Integer> getAllMerchantsSize(@PathVariable String ssid) throws Exception {
        long start = System.currentTimeMillis();
        List<Merchant> merchants = merchantService.findAllMerchantWithSsid(ssid).get();
        long end = System.currentTimeMillis();
        logger.info("Fetching data with ssid {} took {}",ssid ,end-start);
        return new ResponseEntity<>(merchants.size(), HttpStatus.OK);
    }

}
