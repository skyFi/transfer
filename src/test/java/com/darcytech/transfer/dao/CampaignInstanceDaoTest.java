package com.darcytech.transfer.dao;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darcytech.transfer.BaseTest;
import com.darcytech.transfer.TransferMain;
import com.darcytech.transfer.model.CampaignInstance;

/**
 * User: dixi
 * Time: 2015/12/17 17:22
 */
public class CampaignInstanceDaoTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(CampaignInstanceDaoTest.class);

    @Autowired
    private CampaignInstanceDao instanceDao;

    private String pattern = "yyyy-MM-dd HH:mm:ss";
    private Date operateEndTimeLine = DateTime.parse("2015-12-01 14:03:50", DateTimeFormat.forPattern(pattern)).toDate();

    @Test
    public void testFindCampaignInstanceByResultIdIsNull() throws Exception {
        List<CampaignInstance> instanceList = instanceDao.
                findCampaignInstanceByResultIdIsNull(null,operateEndTimeLine, 1, 10);
        logger.info("end time :{} success size:{}", operateEndTimeLine, instanceList.size());
        for (CampaignInstance instance : instanceList) {
            logger.info("instance id :{}", instance.getId());
        }
    }

    @Test
    public void testCountCampaignInstanceByResultIdIsNull() throws Exception {
        int count = instanceDao.countCampaignInstanceByResultIdIsNull(null,operateEndTimeLine);
        logger.info("campaign instance total size :{}", count);
        int pageSize = 100;

        int totalPage = count % pageSize == 0 ? count / pageSize : (count / pageSize) + 1;

        logger.info("campaign instance total size :{},total page :{}", count, totalPage);
    }
}