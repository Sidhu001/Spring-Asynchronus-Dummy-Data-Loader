package com.generatedummydata.SpringDummyDataProject.repository;

import com.generatedummydata.SpringDummyDataProject.entity.Merchant;
import com.generatedummydata.SpringDummyDataProject.entity.MerchantPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MerchantRepository extends JpaRepository<Merchant, MerchantPrimaryKey> {

}
