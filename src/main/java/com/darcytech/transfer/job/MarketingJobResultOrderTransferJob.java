package com.darcytech.transfer.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.dao.RecordDataDao;
import com.darcytech.transfer.dao.TransferEntityDao;
import com.darcytech.transfer.enumeration.RecordTableName;
import com.darcytech.transfer.model.ActionRecord;
import com.darcytech.transfer.model.ActionRecordRecord;
import com.darcytech.transfer.model.MarketingJobResultOrder;
import com.darcytech.transfer.model.MarketingJobResultOrderRecord;
import com.darcytech.transfer.transfer.MarketingJobResultOrderTransferrer;

/**
 * Created by darcy on 2015/12/21.
 */
@Component
public class MarketingJobResultOrderTransferJob extends AbstractTransferJob{


    @Value("${transfer.marketingjob.result.order.worker.count}")
    private int transferMarketingJobResultOrderWorkerCount;

    @Autowired
    private MarketingJobResultOrderTransferrer marketingJobResultOrderTransferrer;

    @Autowired
    private RecordDataDao recordDataDao;

    @Autowired
    private TransferEntityDao transferEntityDao;

    @Override
    protected void saveTransferRecord(String transferDay) throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(transferDay);
        Date end = new DateTime(start).plusDays(1).toDate();
        long prodCount = recordDataDao.count(start, end, MarketingJobResultOrder.class.getSimpleName());

        MarketingJobResultOrderRecord marketingJobResultOrderRecord = new MarketingJobResultOrderRecord();
        marketingJobResultOrderRecord.setTransferDay(transferDay);
        marketingJobResultOrderRecord.setTotalCount(prodCount);
        transferEntityDao.persist(marketingJobResultOrderRecord);
    }

    @Override
    protected void transferByDay(Date day) throws Exception {
        if (day != null) {
            marketingJobResultOrderTransferrer.transferByDay(day);
        } else {
            marketingJobResultOrderTransferrer.transfer();
        }
    }

    @Override
    protected BlockingQueue<Integer> prepareTokenQueue() {
        BlockingQueue<Integer> tokens = new ArrayBlockingQueue<>(transferMarketingJobResultOrderWorkerCount);
        for (int i = 0; i < transferMarketingJobResultOrderWorkerCount; i++) {
            tokens.add(i);
        }
        return tokens;
    }

    @Override
    protected List<String> getTransferDays() {
        return recordDataDao.getTransferredDays(RecordTableName.marketing_job_result_order_record);
    }
}
