package com.darcytech.transfer.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darcytech.transfer.TransferMain;
import com.darcytech.transfer.job.AliasCreateJob;

/**
 * Created by darcy on 2015/12/2.
 */

@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TransferMain.class)
public class AliasCreatorTest {

    @Autowired
    private AliasCreateJob aliasCreateJob;

    @Test
    public void testCreateAliases() throws Exception {
        aliasCreateJob.createAliases();
    }
}
