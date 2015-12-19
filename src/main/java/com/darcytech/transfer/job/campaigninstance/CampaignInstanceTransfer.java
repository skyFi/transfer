package com.darcytech.transfer.job.campaigninstance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.dao.CampaignInstanceDao;
import com.darcytech.transfer.dao.CampaignInstanceNewDao;
import com.darcytech.transfer.dao.CustomerDao;
import com.darcytech.transfer.model.CampaignInstance;
import com.darcytech.transfer.model.CampaignInstanceNew;

/**
 * User: dixi
 * Time: 2015/12/17 18:28
 */
@Component
public class CampaignInstanceTransfer implements CampaignInstanceNewTransfer {
    private static final Logger logger = LoggerFactory.getLogger(CampaignInstanceTransfer.class);

    @Autowired
    private CampaignInstanceDao instanceDao;

    @Autowired
    private CampaignInstanceNewDao instanceNewDao;

    @Autowired
    private CustomerDao customerDao;

    private int pageSize = 1000;

    @Override
    public void transferAndSave(Date startTime, Date endTime) {
        logger.info("transfer campaign instance from instance start ,time range is {} ~ {}", startTime, endTime);
        int totalSize = instanceDao.countCampaignInstanceByResultIdIsNull(startTime, endTime);
        int totalPage = 1;
        if (totalSize > pageSize) {
            totalPage = totalSize % pageSize == 0 ? totalSize / pageSize : (totalSize / pageSize) + 1;
        }

        for (int pageNO = 1; pageNO <= totalPage; pageNO++) {
            List<CampaignInstance> instances = instanceDao
                    .findCampaignInstanceByResultIdIsNull(startTime, endTime, pageNO, pageSize);

            Map<Long, String> customerNickMap = getCustomerNickMap(instances);

            List<CampaignInstanceNew> instanceNews = Lists.newArrayList();
            for (CampaignInstance instance : instances) {
                CampaignInstanceNew instanceNew = new CampaignInstanceNew();
                instanceNew.setBuyerNick(customerNickMap.get(instance.getCustomerId()));
                instanceNew.setBuyerRealName("");
                instanceNew.setCampaignId(instance.getCampaignId());
                instanceNew.setCampaignTypeId(instance.getCampaignTypeId());
                instanceNew.setContacted(true);
                instanceNew.setCreateDate(instance.getCreateDate());
                instanceNew.setStartDate(instance.getStartDate());
                instanceNew.setEndDate(instance.getEndDate());
                instanceNew.setCurrentStep(instance.getCurrentStep());
                instanceNew.setLastStepTriggerTime(instance.getLastStepTriggerTime());
                instanceNew.setMobile(instance.getMobile());
                instanceNew.setStatus(instance.getStatus());
                instanceNew.setSucceed(true);
                instanceNew.setTotalPayment(BigDecimal.ZERO);
                instanceNew.setTradeNum(instance.getTotalTrades().size());
                instanceNew.setUserId(instance.getUserId());

                instanceNews.add(instanceNew);
            }

            instanceNewDao.multiSave(instanceNews);

            logger.info("the campaign instance than result id is null total size:{}, totalPage/current:{}/{} "
                    , totalSize, totalPage, pageNO);

        }

        logger.info("transfer campaign instance from instance end");

    }

    private Map<Long, String> getCustomerNickMap(List<CampaignInstance> instances) {
        Set<Long> customerIds = new HashSet<>();
        for (CampaignInstance instance : instances) {
            customerIds.add(instance.getCustomerId());
        }

        return customerDao.findCustomerByIds(customerIds);
    }
}
