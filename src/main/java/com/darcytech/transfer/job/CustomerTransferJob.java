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
import com.darcytech.transfer.model.CustomerDetail;
import com.darcytech.transfer.model.CustomerRecord;
import com.darcytech.transfer.transfer.CustomerTransferrer;

/**
 * Created by darcy on 2015/12/2.
 */
@Component
public class CustomerTransferJob extends AbstractTransferJob{

    @Autowired
    private CustomerTransferrer customerTransferrer;

    @Value("${transfer.customer.worker.count}")
    private int transferCustomerWorkerCount;

    @Autowired
    private RecordDataDao recordDataDao;

    @Autowired
    private TransferEntityDao transferEntityDao;

    @Override
    protected void saveTransferRecord(String transferDay) throws Exception {

        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(transferDay);
        Date end = new DateTime(start).plusDays(1).toDate();
        long prodCount = recordDataDao.count(start, end, CustomerDetail.class.getSimpleName());

        CustomerRecord customerRecord = new CustomerRecord();
        customerRecord.setTransferDay(transferDay);
        customerRecord.setTotalCount(prodCount);
        transferEntityDao.persist(customerRecord);
    }

    @Override
    protected void transferByDay(Date day) throws Exception {
        if (day != null) {
            customerTransferrer.transferByDay(day);
        }
    }

    @Override
    protected BlockingQueue<Integer> prepareTokenQueue() {
        BlockingQueue<Integer> tokens = new ArrayBlockingQueue<>(transferCustomerWorkerCount);
        for (int i = 0; i < transferCustomerWorkerCount; i++) {
            tokens.add(i);
        }
        return tokens;
    }

    @Override
    protected List<String> getTransferDays() {
        return recordDataDao.getTransferredDays(RecordTableName.customer_record);
    }

}
