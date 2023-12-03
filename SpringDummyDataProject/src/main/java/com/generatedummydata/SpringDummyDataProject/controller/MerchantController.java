package com.generatedummydata.SpringDummyDataProject.controller;

import com.generatedummydata.SpringDummyDataProject.entity.Merchant;
import com.generatedummydata.SpringDummyDataProject.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    MerchantService merchantService;

    @PostMapping(value = "/saveMerchant/{noOfMerchants}", produces = "application/json")
    public ResponseEntity<List<Merchant>> saveMerchant(@PathVariable int noOfMerchants) throws Exception {
        List<Merchant> merchants = merchantService.saveMerchant(noOfMerchants);
        return new ResponseEntity<>(merchants, HttpStatus.OK);
    }

    @PostMapping(value = "/saveAllMerchants/{chunkSize}/{batchSize}/{noOfDummyDataRequired}", produces = "application/json")
    public ResponseEntity<Map<String, Integer>> saveAllMerchants(@PathVariable int chunkSize,
                                                       @PathVariable int batchSize,
                                                       @PathVariable int noOfDummyDataRequired) throws Exception {
        Map<String, Integer> jobSummary = merchantService.saveDummyMerchantDataInBatches(chunkSize, batchSize, noOfDummyDataRequired).get();
        return new ResponseEntity<>(jobSummary, HttpStatus.OK);
    }

}
