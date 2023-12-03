package com.generatedummydata.SpringDummyDataProject.service;


import com.generatedummydata.SpringDummyDataProject.entity.Merchant;
import com.google.gson.JsonArray;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface MerchantService {

    public  CompletableFuture<Map<String, Integer>> saveDummyMerchantDataInBatches(int chunkSize, int noOfParallelBatch, int noOfDummyDataRequired) throws Exception;
    public List<Merchant> saveMerchant(int noOfMerchant) throws Exception;

    public CompletableFuture<JsonArray>  findAllMerchantWithSsid(String ssid) throws Exception;

}
