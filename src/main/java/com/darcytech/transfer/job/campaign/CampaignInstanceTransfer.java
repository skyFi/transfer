package com.darcytech.transfer.job.campaign;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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
public class CampaignInstanceTransfer implements CampaignTransfer {
    private static final Logger logger = LoggerFactory.getLogger(CampaignInstanceTransfer.class);

    @Autowired
    private CampaignInstanceDao instanceDao;

    @Autowired
    private CampaignInstanceNewDao instanceNewDao;

    @Autowired
    private CustomerDao customerDao;

    private int pageSize = 100;

    @Override
    public void transferAndSave(Date startTime, Date endTime) {
        logger.info("transfer campaign instance from instance start ,time range is {} ~ {}", startTime, endTime);
        int totalSize = instanceDao.countCampaignInstanceByDate(startTime, endTime);
        int totalPage = 1;
        if (totalSize > pageSize) {
            totalPage = totalSize % pageSize == 0 ? totalSize / pageSize : (totalSize / pageSize) + 1;
        }

        for (int pageNO = 1; pageNO <= totalPage; pageNO++) {
            List<CampaignInstance> instances = instanceDao
                    .findCampaignInstanceByDate(startTime, endTime, pageNO, pageSize);

            createNewData(instances);

            logger.info("transfer campaign instance total size:{}, totalPage/current:{}/{} "
                    , totalSize, totalPage, pageNO);
        }

        logger.info("transfer campaign instance from instance end");

    }

    private void createNewData(List<CampaignInstance> instances) {
        Map<Long, String> customerNickMap = getCustomerNickMap(instances);

        List<CampaignInstanceNew> instanceNews = Lists.newArrayList();
        for (CampaignInstance instance : instances) {
            try {
                CampaignInstanceNew instanceNew = instanceNewDao.findByOldInstanceId(instance.getId());
                if (instanceNew == null) {
                    instanceNew = new CampaignInstanceNew();
                }
                instanceNew.setOldInstanceId(instance.getId());
                instanceNew.setUserId(instance.getUserId());
                instanceNew.setCampaignId(instance.getCampaignId());
                instanceNew.setCampaignTypeId(instance.getCampaignTypeId());
                instanceNew.setBuyerNick(customerNickMap.get(instance.getCustomerId()));
                instanceNew.setMobile(instance.getMobile());
                instanceNew.setCurrentStep(instance.getCurrentStep());
                instanceNew.setStartDate(instance.getStartDate());
                instanceNew.setEndDate(instance.getEndDate());
                instanceNew.setCreateDate(instance.getCreateDate());
                String resultId = instance.getResultId();
                if (StringUtils.isNotEmpty(resultId)) {
                    instanceNew.setTriggerTradeId(Long.valueOf(resultId.split("_")[1]));
                }

                List<Long> tradeIds = Lists.newArrayList();
                for (String tid : instance.getTotalTrades()) {
                    tradeIds.add(Long.valueOf(tid));
                }
                instanceNew.setTotalTrades(tradeIds);
                instanceNew.setStatus(instance.getStatus());
                instanceNew.setLastStepTriggerTime(instance.getLastStepTriggerTime());
                instanceNews.add(instanceNew);
            } catch (Exception e) {
                logger.error("transfer error instance id = {}", instance.getId());
            }

        }

        instanceNewDao.multiSaveOrUpdate(instanceNews);
    }

    private Map<Long, String> getCustomerNickMap(List<CampaignInstance> instances) {
        Set<Long> customerIds = new HashSet<>();
        for (CampaignInstance instance : instances) {
            customerIds.add(instance.getCustomerId());
        }

        return customerDao.findCustomerByIds(customerIds);
    }
}
