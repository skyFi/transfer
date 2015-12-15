package com.darcytech.transfer.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import junit.framework.TestCase;

public class NewTradeRateDaoTest extends TestCase {

    @Autowired
    private NewTradeRateDao newTradeRateDao;

    @Test
    public void testGetIndexByUserId() throws Exception {
        newTradeRateDao.getIndexByUserId("54520057");
    }
}