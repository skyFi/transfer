package com.darcytech.transfer.job.campaign;

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

import com.darcytech.transfer.dao.ActionRecordIdMappingDao;
import com.darcytech.transfer.dao.CampaignResultDaoEs;
import com.darcytech.transfer.dao.CampaignResultNewDao;
import com.darcytech.transfer.dto.CampaignResult;
import com.darcytech.transfer.dto.StepResult;
import com.darcytech.transfer.exception.EsShardsErrorException;
import com.darcytech.transfer.model.CampaignResultNew;
import com.darcytech.transfer.model.CampaignStepResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * User: dixi
 * Time: 2015/12/17 18:29
 */
@Component
public class CampaignResultTransfer implements CampaignTransfer {
    private static final Logger logger = LoggerFactory.getLogger(CampaignResultTransfer.class);

    @Autowired
    private CampaignResultDaoEs resultDaoEs;

    @Autowired
    private ActionRecordIdMappingDao idMappingDao;

    @Autowired
    private CampaignResultNewDao resultNewDao;

    @Autowired
    private ObjectMapper objectMapper;

    private int pageSize = 100;

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

            List<CampaignResult> results = Lists.newArrayList();
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                CampaignResult result = objectMapper.readValue(hit.getSourceAsString(), CampaignResult.class);
                results.add(result);
            }
            createNewData(results);

            int total = (int) scrollResp.getHits().getTotalHits();
            int totalPage = total % pageSize == 0 ? total / pageSize : (total / pageSize) + 1;
            logger.info("total campaign result size :{},totalPage/current:{}/{}", total, totalPage, pageNo);

            scrollResp = resultDaoEs.SearchByScrollId(scrollResp.getScrollId());

        }
    }

    private void createNewData(List<CampaignResult> resultList) {
        Set<String> actionRecordIds = getActionRecordIds(resultList);
        if (actionRecordIds.size() == 0) {
            return;
        }

        Map<String, Long> actionRecordIdMapping = idMappingDao.getIdByOldIds(actionRecordIds);

        for (CampaignResult result : resultList) {
            CampaignResultNew resultNew = resultNewDao.findByOldResultId(result.getId());
            if (resultNew == null) {
                resultNew = new CampaignResultNew();
            }

            resultNew.setUserId(result.getUserId());
            resultNew.setCampaignId(result.getCampaignId());
            resultNew.setCampaignTypeId(result.getCampaignTypeId());
            resultNew.setBuyerNick(result.getBuyerNick());
            resultNew.setBuyerPhone(result.getBuyerPhone());
            resultNew.setBuyerRealName(result.getBuyerRealName());
            resultNew.setTriggerTradeId(Long.valueOf(result.getId().split("_")[1]));
            resultNew.setTradeNum(result.getTradeNum());
            List<Long> totalTrades = Lists.newArrayList();
            for (String tid : result.getTotalTrades()) {
                totalTrades.add(Long.valueOf(tid));
            }
            resultNew.setTotalTrades(totalTrades);
            resultNew.setTotalPayment(result.getTotalPayment());
            resultNew.setCampaignTime(result.getCampaignTime());
            resultNew.setContacted(result.getContacted());
            resultNew.setSuccess(result.getSuccess());

            List<CampaignStepResult> campaignStepResults = Lists.newArrayList();
            for (StepResult stepResult : result.getStepResultList()) {
                CampaignStepResult campaignStepResult = new CampaignStepResult();
                campaignStepResult.setStepIndex(stepResult.getStepIndex());
                campaignStepResult.setStepName(stepResult.getStepName());
                campaignStepResult.setCreated(stepResult.getCreated());
                campaignStepResult.setTriggerTradeId(Long.valueOf(stepResult.getTriggerTradeId()));
                campaignStepResult.setContacted(result.getContacted());

                Set<Long> stepTotalTrades = Sets.newHashSet();
                for (String tid : stepResult.getTotalTrades()) {
                    stepTotalTrades.add(Long.valueOf(tid));
                }
                campaignStepResult.setTotalTrades(stepTotalTrades);

                for (String actionRecordOId : stepResult.getActionRecords()) {
                    campaignStepResult.getActionRecords().add(actionRecordIdMapping.get(actionRecordOId));
                }

                campaignStepResults.add(campaignStepResult);
            }
            resultNew.setCampaignStepResults(campaignStepResults);
            resultNew.setLastModifyTime(result.getLastModifyTime());
            resultNew.setOldResultId(result.getId());

            resultNewDao.saveOrUpdate(resultNew);
        }
    }

    private Set<String> getActionRecordIds(List<CampaignResult> resultList) {
        Set<String> ids = Sets.newHashSet();
        for (CampaignResult result : resultList) {
            for (StepResult stepResult : result.getStepResultList()) {
                ids.addAll(stepResult.getActionRecords());
            }
        }
        return ids;
    }
}
