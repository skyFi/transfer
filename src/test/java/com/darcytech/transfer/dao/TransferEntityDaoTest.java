package com.darcytech.transfer.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.darcytech.transfer.model.MarketingJobResult;
import com.darcytech.transfer.test.BaseTest;

public class TransferEntityDaoTest extends BaseTest {

    private TransferEntityDao transferEntityDao;

    @Test
    public void testSave() throws Exception {
        MarketingJobResult marketingJobResult = new MarketingJobResult();
        marketingJobResult.setBuyerNick("ee");
        marketingJobResult.setJobId(5544L);
        List<MarketingJobResult> marketingJobResults = new ArrayList<>();
        marketingJobResults.add(marketingJobResult);

        transferEntityDao.save(marketingJobResults);

    }
}