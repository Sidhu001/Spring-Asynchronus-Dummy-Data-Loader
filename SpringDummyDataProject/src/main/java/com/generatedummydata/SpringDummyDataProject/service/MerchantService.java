package com.generatedummydata.SpringDummyDataProject.service;


import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.generatedummydata.SpringDummyDataProject.dto.MerchantDTO;
import com.generatedummydata.SpringDummyDataProject.entity.Merchant;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import java.util.concurrent.Future;

public interface MerchantService {

    public  CompletableFuture<Map<String, Integer>> saveDummyMerchantData(int chunkSize, int noOfParallelBatch, int noOfDummyDataRequired) throws Exception;
    public List<Merchant> saveMerchant(int noOfMerchant) throws Exception;

    public CompletableFuture<JsonArray>  findAllMerchantWithSsid(String ssid) throws Exception;

}
