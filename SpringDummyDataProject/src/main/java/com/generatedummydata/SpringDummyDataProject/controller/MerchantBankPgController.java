package com.generatedummydata.SpringDummyDataProject.controller;

import com.generatedummydata.SpringDummyDataProject.entity.postgres.MerchantBankPostgres;
import com.generatedummydata.SpringDummyDataProject.service.MerchantServicePostgres;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/merchantpg")
public class MerchantBankPgController {

    @Autowired
    MerchantServicePostgres merchantServicePostgres;

    @PostMapping(value = "/savemerchant", produces = "application/json")
    public ResponseEntity<String> saveMerchant() throws Exception{
        String savedMerchant = merchantServicePostgres.saveMerchant();
        return new ResponseEntity<>(savedMerchant, HttpStatus.OK);
    }
}
