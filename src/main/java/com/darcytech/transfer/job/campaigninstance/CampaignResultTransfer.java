package com.darcytech.transfer.job.campaigninstance;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.dao.CampaignInstanceDao;
import com.darcytech.transfer.dao.CampaignInstanceNewDao;
import com.darcytech.transfer.dao.CampaignResultDaoEs;
import com.darcytech.transfer.dto.CampaignResult;
import com.darcytech.transfer.dto.StepResult;
import com.darcytech.transfer.exception.EsShardsErrorException;
import com.darcytech.transfer.model.CampaignInstance;
import com.darcytech.transfer.model.CampaignInstanceNew;
import com.darcytech.transfer.model.CampaignStepInstance;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * User: dixi
 * Time: 2015/12/17 18:29
 */
@Component
public class CampaignResultTransfer implements CampaignInstanceNewTransfer {
    private static final Logger logger = LoggerFactory.getLogger(CampaignResultTransfer.class);

    @Autowired
    private CampaignResultDaoEs resultDaoEs;

    @Autowired
    private CampaignInstanceDao instanceDao;

    @Autowired
    private CampaignInstanceNewDao instanceNewDao;

    @Autowired
    private ObjectMapper objectMapper;

    private int pageSize = 1000;

    @Override
    public void transferAndSave(Date startTime, Date endTime) throws IOException {
        logger.info("transfer campaign instance from campaign result start ,time range is {} ~ {}", startTime, endTime);

        logger.info("transfer campaign instance from campaign result than last modify time is null start");
        SearchResponse notExistTimeScrollResp = resultDaoEs.searchScrollByLastModifyTimeIsNull(pageSize);
        scrollTransAndSave(notExistTimeScrollResp);
        logger.info("transfer campaign instance from campaign result than last modify time is null end");

        logger.info("transfer campaign instance from campaign result than last modify time is not null start");
        SearchResponse scrollResp = resultDaoEs.searchScrollByLastModifyTime(startTime, endTime, pageSize);
        scrollTransAndSave(scrollResp);
        logger.info("transfer campaign instance from campaign result than last modify time is not null end");

    }

    private void scrollTransAndSave(SearchResponse scrollResp) throws IOException {
        int pageNo = 0;
        while (true) {
            int shards = scrollResp.getTotalShards();
            if (shards != 16) {
                throw new EsShardsErrorException("total shards is  " + shards + " when scroll campaign result");
            }

            if (scrollResp.getHits().getHits().length == 0) {
                break;
            }

            pageNo++;
            List<CampaignResult> finishResults = Lists.newArrayList();
            List<CampaignResult> notFinishResults = Lists.newArrayList();
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                CampaignResult result = objectMapper.readValue(hit.getSourceAsString(), CampaignResult.class);
                if (result.getSuccess() != null) {
                    finishResults.add(result);
                } else {
                    notFinishResults.add(result);
                }
            }
            transferByFinishResultsAndSave(finishResults);
            transferByNotFinishResultsAndUpdate(notFinishResults);

            int total = (int) scrollResp.getHits().getTotalHits();
            int totalPage = total % pageSize == 0 ? total / pageSize : (total / pageSize) + 1;
            logger.info("total campaign result size :{},totalPage/current:{}/{}", total, totalPage, pageNo);

            scrollResp = resultDaoEs.SearchByScrollId(scrollResp.getScrollId());

        }
    }

    private void transferByFinishResultsAndSave(List<CampaignResult> resultList) {
        Set<String> resultIds = getResultIds(resultList);
        if (resultIds.size() == 0) {
            return;
        }

        Map<String, CampaignInstance> resultInstanceMap = instanceDao.findByResultIds(resultIds);

        List<CampaignInstanceNew> instanceNews = Lists.newArrayList();
        for (CampaignResult result : resultList) {
            CampaignInstanceNew instanceNew = new CampaignInstanceNew();
            fullingValue(resultInstanceMap, result, instanceNew);

            instanceNews.add(instanceNew);
        }

        instanceNewDao.multiSave(instanceNews);
    }

    private void transferByNotFinishResultsAndUpdate(List<CampaignResult> resultList) {
        Set<String> resultIds = getResultIds(resultList);
        if (resultIds.size() == 0) {
            return;
        }

        Map<String, CampaignInstance> resultInstanceMap = instanceDao.findByResultIds(resultIds);

        for (CampaignResult result : resultList) {
            CampaignInstanceNew instanceNew = instanceNewDao.
                    findByConditions(result.getUserId(), result.getCampaignId(), result.getBuyerNick());

            if (instanceNew == null) {
                instanceNew = new CampaignInstanceNew();
            }
            fullingValue(resultInstanceMap, result, instanceNew);

            instanceNewDao.saveOrUpdate(instanceNew);
        }
    }

    private void fullingValue(Map<String, CampaignInstance> resultInstanceMap,
                              CampaignResult result,
                              CampaignInstanceNew instanceNew) {
        instanceNew.setBuyerNick(result.getBuyerNick());
        instanceNew.setBuyerRealName(result.getBuyerRealName());
        instanceNew.setCampaignId(result.getCampaignId());
        instanceNew.setCampaignTypeId(result.getCampaignTypeId());
        instanceNew.setContacted(result.getContacted());

        CampaignInstance instance = resultInstanceMap.get(result.getId());
        if (instance != null) {
            instanceNew.setCreateDate(instance.getCreateDate());
            instanceNew.setStartDate(instance.getStartDate());
            instanceNew.setEndDate(instance.getEndDate());
            instanceNew.setCurrentStep(instance.getCurrentStep());
            instanceNew.setLastStepTriggerTime(instance.getLastStepTriggerTime());
            instanceNew.setStatus(instance.getStatus());
        }

        instanceNew.setMobile(result.getBuyerPhone());
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
            Set<Long> tids = Sets.newHashSet();
            for (String id : step.getTotalTrades()) {
                tids.add(Long.valueOf(id));
            }
            stepInstance.setTotalTrades(tids);
            stepInstance.setTriggerTradeId(step.getTriggerTradeId());
            instanceNew.getStepInstances().add(stepInstance);
        }
    }

    private Set<String> getResultIds(List<CampaignResult> resultList) {
        Set<String> resultIds = Sets.newHashSet();
        for (CampaignResult result : resultList) {
            resultIds.add(result.getId());
        }
        return resultIds;
    }
}
