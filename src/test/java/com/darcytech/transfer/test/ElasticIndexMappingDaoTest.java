package com.darcytech.transfer.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darcytech.transfer.TransferMain;
import com.darcytech.transfer.dao.ElasticIndexMappingDao;

/**
 * Created by darcy on 2015/12/2.
 */
@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TransferMain.class)
public class ElasticIndexMappingDaoTest {

    @Autowired
    private ElasticIndexMappingDao elasticIndexMappingDao;

    @Test
    public void testGetIndexDocCounts() throws IOException {
        Map<String, Long> indexDocCounts = elasticIndexMappingDao.getIndexDocCounts();
        Assert.assertFalse(indexDocCounts.isEmpty());
        Assert.assertEquals(indexDocCounts.get("index_0"), new Long(60587));
    }
}
