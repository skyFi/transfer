package com.darcytech.transfer.dao;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.darcytech.transfer.BaseTest;
import com.google.common.collect.Sets;

/**
 * User: dixi
 * Time: 2015/12/18 16:03
 */
public class CustomerDaoTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(CustomerDaoTest.class);

    @Autowired
    private CustomerDao customerDao;

    @Test
    public void testFindCustomerByIds() throws Exception {
        Set set = Sets.newHashSet(1L, 11L, 111L, 1111L);
        Map<String, Object> ret = customerDao.findCustomerByIds(set);
        logger.info("ret={}", ret.toString());
    }
}