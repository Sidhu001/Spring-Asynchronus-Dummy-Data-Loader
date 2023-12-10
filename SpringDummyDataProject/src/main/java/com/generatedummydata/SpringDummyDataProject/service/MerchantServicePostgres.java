package com.generatedummydata.SpringDummyDataProject.service;

import com.generatedummydata.SpringDummyDataProject.entity.mysql.Merchant;
import com.generatedummydata.SpringDummyDataProject.entity.postgres.MerchantBankPostgres;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface MerchantServicePostgres {

    public String saveMerchant() throws Exception;

    public List<Merchant> fetchMerchants(String ssid) throws Exception;

    public Map<String, Integer> saveAllMerchantsToDb(String ssId, int batchSize, int batchUpsertSize, int noOfParallelBatchInsets) throws Exception;
}
