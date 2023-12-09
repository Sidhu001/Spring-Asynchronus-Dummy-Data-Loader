package com.generatedummydata.SpringDummyDataProject.service;

import com.generatedummydata.SpringDummyDataProject.entity.mysql.Merchant;
import com.generatedummydata.SpringDummyDataProject.entity.postgres.MerchantBankPostgres;
import com.generatedummydata.SpringDummyDataProject.entity.postgres.MerchantBankPostgresPrimaryKey;
import com.generatedummydata.SpringDummyDataProject.repository.postgresrepo.MerchantPostgresRepository;
import com.generatedummydata.SpringDummyDataProject.utils.RandomDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.generatedummydata.SpringDummyDataProject.constants.TableConstants.*;
import static com.generatedummydata.SpringDummyDataProject.utils.RandomDataGenerator.*;
import static com.generatedummydata.SpringDummyDataProject.utils.RandomDataGenerator.generateRandomDateTimeStamp;

@Service("merchantServicePostgresImpl")
public class MerchantServicePostgresImpl implements MerchantServicePostgres {

    @Autowired
    MerchantPostgresRepository merchantPostgresRepository;

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private JdbcTemplate postgresJdbcTemplate;

    @Override
    public String saveMerchant() throws Exception {
        List<MerchantBankPostgres> merchantToBeSaved = new ArrayList<>();

        String query =
                "insert into merchant_bank (agr_day_to_pay,bank_acct_nm,cr_bank_acct_cd,cr_bank_acct_no,cr_bank_acct_swift_no,dr_bank_acct_cd,dr_bank_acct_no," +
                        "dr_bank_acct_swift_no,end_dt,lst_updt_ts,min_settle_am,payee_nm,rec_creat_ts,strt_dt,trs_fsi_acct_no,trs_fsi_refer_no,ain_no,bus_ctr_cd,mer_id," +
                        "settle_acct_type_cd,srce_sys_id) values (?,?,?,?,?,?,?,?,?,?,to_timestamp(?, 'YYYY-MM-DD'),?,?,to_timestamp(?, 'YYYY-MM-DD'),?,?,?,?,?,?,?)";

        String queryWithParams =
                "insert into merchant_bank (agr_day_to_pay,bank_acct_nm,cr_bank_acct_cd,cr_bank_acct_no,cr_bank_acct_swift_no," +
                        "dr_bank_acct_cd,dr_bank_acct_no,dr_bank_acct_swift_no,end_dt,lst_updt_ts,min_settle_am,payee_nm,rec_creat_ts,strt_dt," +
                        "trs_fsi_acct_no,trs_fsi_refer_no,ain_no,bus_ctr_cd,mer_id,settle_acct_type_cd,srce_sys_id) values (31,'1f83e0768c724a7eb941cfe86adc1c'," +
                        "'7a9d57e','628234854436411','29425591382','150293f','784815598755657','18380565290','2001-12-16',to_timestamp('2012-08-16 01:15:57.489000','YYYY-MM-DD')," +
                        "384560533602304,'d58339ab88594742b1956abe4',to_timestamp('2012-01-15 03:19:45.730000','YYYY-MM-DD') ,'2010-01-04','" +
                        "7880963137','2266585759','11111111111','016','8467898439','002','F25')";
        postgresJdbcTemplate.execute(queryWithParams);
        return "Saved";
    }
}
