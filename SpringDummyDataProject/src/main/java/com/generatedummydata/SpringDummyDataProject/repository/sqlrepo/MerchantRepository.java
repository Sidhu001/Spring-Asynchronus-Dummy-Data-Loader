package com.generatedummydata.SpringDummyDataProject.repository.sqlrepo;

import com.generatedummydata.SpringDummyDataProject.entity.mysql.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, MerchantPrimaryKey> {
    List<Merchant> findBySrcSysId(String srceSysId) throws Exception;
}
