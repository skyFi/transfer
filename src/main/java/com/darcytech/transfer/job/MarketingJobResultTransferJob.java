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
import com.darcytech.transfer.enumeration.RecordType;
import com.darcytech.transfer.model.MarketingJobResult;
import com.darcytech.transfer.model.Record;
import com.darcytech.transfer.transfer.MarketingJobResultTransferrer;

/**
 * Created by darcy on 2015/12/19.
 */
@Component
public class MarketingJobResultTransferJob extends AbstractTransferJob{

    @Value("${transfer.marketingjob.result.worker.count}")
    private int transferMarketingJobResultWorkerCount;

    @Autowired
    private MarketingJobResultTransferrer marketingJobResultTransferrer;

    @Autowired
    private RecordDataDao recordDataDao;

    @Autowired
    private TransferEntityDao transferEntityDao;

    @Override
    protected void saveTransferRecord(String transferDay) throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(transferDay);
        Date end = new DateTime(start).plusDays(1).toDate();
        long prodCount = recordDataDao.count(start, end, MarketingJobResult.class.getSimpleName());

        Record marketingJobResultRecord = new Record();
        marketingJobResultRecord.setTransferDay(transferDay);
        marketingJobResultRecord.setTotalCount(prodCount);
        marketingJobResultRecord.setRecordType(RecordType.MARKETINGJOBRESULT);
        transferEntityDao.persist(marketingJobResultRecord);
    }

    @Override
    protected void transferByDay(Date day) throws Exception {
        if (day != null) {
            marketingJobResultTransferrer.transferByDay(day);
        } else {
            marketingJobResultTransferrer.transfer();
        }
    }

    @Override
    protected BlockingQueue<Integer> prepareTokenQueue() {
        BlockingQueue<Integer> tokens = new ArrayBlockingQueue<>(transferMarketingJobResultWorkerCount);
        for (int i = 0; i < transferMarketingJobResultWorkerCount; i++) {
            tokens.add(i);
        }
        return tokens;
    }

    @Override
    protected List<String> getTransferDays() {
        return recordDataDao.getTransferredDays(RecordTableName.marketing_job_result_record);
    }
}
