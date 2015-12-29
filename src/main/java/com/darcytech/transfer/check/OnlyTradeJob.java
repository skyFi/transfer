package com.darcytech.transfer.check;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.model.CustomerDetail;
import com.darcytech.transfer.recorder.TransferRecorder;

/**
 * Created by darcy on 2015/12/25.
 */
@Component
public class OnlyTradeJob {

    private final Logger logger = LoggerFactory.getLogger(OnlyTradeJob.class);

    @Autowired
    private TransferRecorder transferRecorder;

    @Autowired
    private CheckNewTradeDao checkNewTradeDao;

    public void showDirtyCustomer() throws Exception{

        File onlyTrade = new File("only-trade.rcd");

        List<CustomerDetail> customerDetails = checkNewTradeDao.getDirtyData();
        for (CustomerDetail customerDetail : customerDetails) {

            transferRecorder.writeRecord(String.valueOf(customerDetail), onlyTrade);
            logger.debug("only have trade customer data: {}. ",
                    String.valueOf(customerDetail));
        }
    }
}
