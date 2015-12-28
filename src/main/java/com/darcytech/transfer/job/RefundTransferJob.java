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
import com.darcytech.transfer.model.MarketingJobResult;
import com.darcytech.transfer.model.MarketingJobResultRecord;
import com.darcytech.transfer.model.Refund;
import com.darcytech.transfer.model.RefundRecord;
import com.darcytech.transfer.transfer.RefundTransferrer;

/**
 * Created by darcy on 2015/12/22.
 */
@Component
public class RefundTransferJob extends AbstractTransferJob{

    @Value("${transfer.refund.count}")
    private int transferRefundCount;

    @Autowired
    private RefundTransferrer refundTransferrer;

    @Autowired
    private RecordDataDao recordDataDao;

    @Autowired
    private TransferEntityDao transferEntityDao;

    @Override
    protected void saveTransferRecord(String transferDay) throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(transferDay);
        Date end = new DateTime(start).plusDays(1).toDate();
        long prodCount = recordDataDao.count(start, end, Refund.class.getSimpleName());

        RefundRecord refundRecord = new RefundRecord();
        refundRecord.setTransferDay(transferDay);
        refundRecord.setTotalCount(prodCount);
        transferEntityDao.persist(refundRecord);
    }

    @Override
    protected void transferByDay(Date day) throws Exception {
        if (day != null) {
            refundTransferrer.transferByDay(day);
        }
    }

    @Override
    protected BlockingQueue<Integer> prepareTokenQueue() {
        BlockingQueue<Integer> tokens = new ArrayBlockingQueue<>(transferRefundCount);
        for (int i = 0; i < transferRefundCount; i++) {
            tokens.add(i);
        }
        return tokens;
    }

    @Override
    protected List<String> getTransferDays() {
        return recordDataDao.getTransferredDays(RecordTableName.refund_record);
    }
}
