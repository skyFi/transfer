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
import com.darcytech.transfer.model.ActionRecord;
import com.darcytech.transfer.model.Record;
import com.darcytech.transfer.transfer.ActionRecordTransferrer;

/**
 * Created by darcy on 2015/12/21.
 */
@Component
public class ActionRecordTransferJob extends AbstractTransferJob{

    @Value("${transfer.action.record.count}")
    private int transferActionRecordCount;

    @Autowired
    private ActionRecordTransferrer actionRecordTransferrer;

    @Autowired
    private RecordDataDao recordDataDao;

    @Autowired
    private TransferEntityDao transferEntityDao;

    @Override
    protected void saveTransferRecord(String transferDay) throws Exception {
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(transferDay);
        Date end = new DateTime(start).plusDays(1).toDate();
        long prodCount = recordDataDao.count(start, end, ActionRecord.class.getSimpleName());

        Record actionRecordRecord = new Record();
        actionRecordRecord.setTransferDay(transferDay);
        actionRecordRecord.setTotalCount(prodCount);
        actionRecordRecord.setRecordType(RecordType.ACTIONRECORD);
        transferEntityDao.persist(actionRecordRecord);
    }

    @Override
    protected void transferByDay(Date day) throws Exception {
        if (day != null) {
            actionRecordTransferrer.transferByDay(day);
        } else {
            actionRecordTransferrer.transfer();
        }
    }

    @Override
    protected BlockingQueue<Integer> prepareTokenQueue() {
        BlockingQueue<Integer> tokens = new ArrayBlockingQueue<>(transferActionRecordCount);
        for (int i = 0; i < transferActionRecordCount; i++) {
            tokens.add(i);
        }
        return tokens;
    }

    @Override
    protected List<String> getTransferDays() {
        return recordDataDao.getTransferredDays(RecordTableName.action_record_record);
    }
}
