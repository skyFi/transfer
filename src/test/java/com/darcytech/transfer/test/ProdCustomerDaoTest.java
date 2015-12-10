package com.darcytech.transfer.test;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darcytech.transfer.TransferMain;
import com.darcytech.transfer.dao.ProdCustomerDao;

import junit.framework.Assert;

/**
 * Created by darcy on 2015/12/2.
 */

@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TransferMain.class)
public class ProdCustomerDaoTest {

    @Autowired
    private ProdCustomerDao prodCustomerDao;

    @Test
    public void testGetUserDocCounts() throws IOException {
        Map<String, Long> userDocCounts = prodCustomerDao.getUserDocCounts();
        Assert.assertFalse(userDocCounts.isEmpty());
        Assert.assertEquals(userDocCounts.get("54520057"), new Long(768));
    }
}
