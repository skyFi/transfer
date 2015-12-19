package com.darcytech.transfer.job.campaigninstance;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.dao.CampaignInstanceDao;
import com.darcytech.transfer.dao.CampaignInstanceNewDao;
import com.darcytech.transfer.dao.CampaignResultDaoEs;
import com.darcytech.transfer.dto.CampaignResult;
import com.darcytech.transfer.dto.StepResult;
import com.darcytech.transfer.model.CampaignInstance;
import com.darcytech.transfer.model.CampaignInstanceNew;
import com.darcytech.transfer.model.CampaignStepInstance;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * User: dixi
 * Time: 2015/12/17 18:29
 */
@Component
public class CampaignResultTransfer implements CampaignInstanceNewTransfer {

    @Autowired
    private CampaignResultDaoEs resultDaoEs;

    @Autowired
    private CampaignInstanceDao instanceDao;

    @Autowired
    private CampaignInstanceNewDao instanceNewDao;

    private int pageSize = 1000;

    @Override
    public void transferAndSave(Date startTime, Date endTime) {
        List<CampaignResult> resultList = resultDaoEs.searchScrollByLastModifyTime(startTime, endTime, pageSize);

        Set<String> resultIds = getResultIds(resultList);
        Map<String, CampaignInstance> resultInstanceMap = instanceDao.findByResultIds(resultIds);

        List<CampaignInstanceNew> instanceNews = Lists.newArrayList();
        for (CampaignResult result : resultList) {
            CampaignInstanceNew instanceNew = new CampaignInstanceNew();
            instanceNew.setBuyerNick(result.getBuyerNick());
            instanceNew.setBuyerRealName(result.getBuyerRealName());
            instanceNew.setCampaignId(result.getCampaignId());
            instanceNew.setCampaignTypeId(result.getCampaignTypeId());
            instanceNew.setContacted(result.getContacted());

            CampaignInstance instance = resultInstanceMap.get(result.getId());
            instanceNew.setCreateDate(instance.getCreateDate());
            instanceNew.setStartDate(instance.getStartDate());
            instanceNew.setEndDate(instance.getEndDate());
            instanceNew.setCurrentStep(instance.getCurrentStep());
            instanceNew.setLastStepTriggerTime(instance.getLastStepTriggerTime());
            instanceNew.setMobile(instance.getMobile());
            instanceNew.setStatus(instance.getStatus());

            instanceNew.setSucceed(result.getSuccess());
            instanceNew.setTotalPayment(result.getTotalPayment());
            instanceNew.setTradeNum(result.getTradeNum());
            instanceNew.setUserId(result.getUserId());

            for (StepResult step : result.getStepResultList()) {
                CampaignStepInstance stepInstance = new CampaignStepInstance();
                stepInstance.setActionRecords(step.getActionRecords());
                stepInstance.setContacted(step.getContacted());
                stepInstance.setCreateDate(step.getCreated());
                stepInstance.setStepIndex(step.getStepIndex());
                stepInstance.setStepName(step.getStepName());
                stepInstance.setTotalTrades(step.getTotalTrades());
                stepInstance.setTriggerTradeId(step.getTriggerTradeId());
                instanceNew.getStepInstances().add(stepInstance);
            }

            instanceNews.add(instanceNew);
        }

        instanceNewDao.multiSave(instanceNews);

    }

    private Set<String> getResultIds(List<CampaignResult> resultList) {
        Set<String> resultIds = Sets.newHashSet();
        for (CampaignResult result : resultList) {
            resultIds.add(result.getId());
        }
        return resultIds;
    }
}
