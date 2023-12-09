package com.generatedummydata.SpringDummyDataProject.repository.postgresrepo;

import com.generatedummydata.SpringDummyDataProject.entity.mysql.Merchant;
import com.generatedummydata.SpringDummyDataProject.entity.postgres.MerchantBankPostgres;
import com.generatedummydata.SpringDummyDataProject.entity.postgres.MerchantBankPostgresPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantPostgresRepository extends JpaRepository<MerchantBankPostgres, MerchantBankPostgresPrimaryKey> {

}
