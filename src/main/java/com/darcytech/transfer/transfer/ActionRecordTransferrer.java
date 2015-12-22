package com.darcytech.transfer.transfer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.dao.EsScroller;
import com.darcytech.transfer.dao.ProdActionRecordDao;
import com.darcytech.transfer.dao.SaveActionRecordDao;
import com.darcytech.transfer.model.ActionRecord;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by darcy on 2015/12/21.
 */
@Component
public class ActionRecordTransferrer extends AbstractTransferrer{

    @Autowired
    private ProdActionRecordDao prodActionRecordDao;

    @Autowired
    private SaveActionRecordDao saveActionRecordDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void bulkTransfer(SearchHits searchHits) throws Exception {

        List<ActionRecord> actionRecords = new ArrayList<>();
        for (SearchHit searchHit : searchHits) {
            ActionRecord actionRecord = objectMapper.readValue(searchHit.getSourceAsString(), ActionRecord.class);

            actionRecord.setOldId(searchHit.getId());
            actionRecords.add(actionRecord);

        }
        saveActionRecordDao.save(actionRecords);
    }

    @Override
    protected EsScroller getScroller(Date start, Date end) {
        if (end != null) {
            return prodActionRecordDao.prepareScrollByDate(start, end);
        } else {
            return prodActionRecordDao.prepareScroll();
        }
    }

}
