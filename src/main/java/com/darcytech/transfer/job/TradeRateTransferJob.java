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
import com.darcytech.transfer.model.Refund;
import com.darcytech.transfer.model.RefundRecord;
import com.darcytech.transfer.model.TradeRate;
import com.darcytech.transfer.model.TradeRateRecord;
import com.darcytech.transfer.transfer.TradeRateTransferrer;

/**
 * Created by darcy on 2015/12/3.
 */
@Component
public class TradeRateTransferJob extends AbstractTransferJob{

    @Value("${transfer.trade.rate.worker.count}")
    private int transferTradeRateWorkerCount;

    @Autowired
    private TradeRateTransferrer tradeRateTransferrer;

    @Autowired
    private RecordDataDao recordDataDao;

    @Autowired
    private TransferEntityDao transferEntityDao;

    @Override
    protected void saveTransferRecord(String transferDay) throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(transferDay);
        Date end = new DateTime(start).plusDays(1).toDate();
        long prodCount = recordDataDao.count(start, end, TradeRate.class.getSimpleName());

        TradeRateRecord tradeRateRecord = new TradeRateRecord();
        tradeRateRecord.setTransferDay(transferDay);
        tradeRateRecord.setTotalCount(prodCount);
        transferEntityDao.persist(tradeRateRecord);
    }

    @Override
    protected void transferByDay(Date day) throws Exception {
        if (day != null) {
            tradeRateTransferrer.transferByDay(day);
        }
    }

    @Override
    protected BlockingQueue<Integer> prepareTokenQueue() {
        BlockingQueue<Integer> tokens = new ArrayBlockingQueue<>(transferTradeRateWorkerCount);
        for (int i = 0; i < transferTradeRateWorkerCount; i++) {
            tokens.add(i);
        }
        return tokens;
    }

    @Override
    protected List<String> getTransferDays() {
        return recordDataDao.getTransferredDays(RecordTableName.trade_rate_record);
    }
}
