package com.darcytech.transfer.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darcytech.transfer.TransferMain;
import com.darcytech.transfer.dao.NewCustomerDao;
import com.darcytech.transfer.model.CustomerDetail;

/**
 * Created by darcy on 2015/12/1.
 */

@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TransferMain.class)
public class BulkSaveTest {

    @Autowired
    private NewCustomerDao newCustomerDao;

    @Test
    public void testBulkSave() throws Exception {
        List<CustomerDetail> customerDetails = new ArrayList<>();

        CustomerDetail customerDetail1 = new CustomerDetail();

        customerDetail1.setUserId(761679524);
        customerDetail1.setId("761679524_smz江西1");

        customerDetails.add(customerDetail1);

        newCustomerDao.bulkSave(customerDetails);

    }
}
