package com.generatedummydata.SpringDummyDataProject.repository;

import com.generatedummydata.SpringDummyDataProject.entity.Merchant;
import com.generatedummydata.SpringDummyDataProject.entity.MerchantPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MerchantRepository extends JpaRepository<Merchant, MerchantPrimaryKey> {
    List<Merchant> findBySrcSysId(String srceSysId) throws Exception;
}
