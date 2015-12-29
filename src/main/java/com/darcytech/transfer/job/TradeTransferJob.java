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
import com.darcytech.transfer.model.Record;
import com.darcytech.transfer.model.Trade;
import com.darcytech.transfer.transfer.TradeTransferrer;

/**
 * Created by darcy on 2015/12/3.
 */
@Component
public class TradeTransferJob extends AbstractTransferJob{

    @Value("${transfer.trade.worker.count}")
    private int transferTradeWorkerCount;

    @Autowired
    private TradeTransferrer tradeTransferrer;

    @Autowired
    private RecordDataDao recordDataDao;

    @Autowired
    private TransferEntityDao transferEntityDao;

    @Override
    protected void saveTransferRecord(String transferDay) throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(transferDay);
        Date end = new DateTime(start).plusDays(1).toDate();
        long prodCount = recordDataDao.count(start, end, Trade.class.getSimpleName());

        Record tradeRecord = new Record();
        tradeRecord.setTransferDay(transferDay);
        tradeRecord.setTotalCount(prodCount);
        tradeRecord.setRecordType(RecordType.TRADERATE);
        transferEntityDao.persist(tradeRecord);
    }

    @Override
    protected void transferByDay(Date day) throws Exception {
        if (day != null) {
            tradeTransferrer.transferByDay(day);
        }
    }

    @Override
    protected BlockingQueue<Integer> prepareTokenQueue() {
        BlockingQueue<Integer> tokens = new ArrayBlockingQueue<>(transferTradeWorkerCount);
        for (int i = 0; i < transferTradeWorkerCount; i++) {
            tokens.add(i);
        }
        return tokens;
    }

    @Override
    protected List<String> getTransferDays() {
        return recordDataDao.getTransferredDays(RecordTableName.trade_record);
    }
}
