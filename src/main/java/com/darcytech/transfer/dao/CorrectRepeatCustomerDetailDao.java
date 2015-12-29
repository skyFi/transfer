package com.darcytech.transfer.dao;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.darcytech.transfer.model.CustomerDetail;
import com.darcytech.transfer.model.RepeatCustomerDetail;
import com.darcytech.transfer.recorder.TransferRecorder;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by darcy on 2015/12/28.
 */
@Repository
public class CorrectRepeatCustomerDetailDao {

    private final Logger logger = LoggerFactory.getLogger(CorrectRepeatCustomerDetailDao.class);

    private File repeatCustomerFile = new File("query_result.csv");

    @Autowired
    private TransferRecorder transferRecorder;

    @Autowired
    private ProdCustomerDao prodCustomerDao;

    @Autowired
    private TransferEntityDao transferEntityDao;

    @Autowired
    private ObjectMapper objectMapper;

    public void correctRepeatData() throws IOException {
        List<String> repeatCustomers = transferRecorder.readRecord(repeatCustomerFile);

        long count = 0;
        for (String repeatCustomer : repeatCustomers) {

            count  = count + 1;
            Long userId = Long.valueOf(repeatCustomer.split(",")[0]);
            String nick = repeatCustomer.split(",")[1].replaceAll("^\"|\"$", "");
            List<CustomerDetail> repeat = prodCustomerDao.getCustomerDetail(userId, nick);

            CustomerDetail customer = null;
            for (CustomerDetail customerDetail : repeat) {
                if (customer == null) {
                    customer = customerDetail;
                } else if (customerDetail.getLastModifyTime().before(customer.getLastModifyTime())) {
                    deleteRepeatCustomerDetail(customerDetail);
                } else {
                    deleteRepeatCustomerDetail(customer);
                    customer = customerDetail;
                }
            }
            logger.debug("correcting customer detail. progress: {}/{}", count, repeatCustomers.size());
        }
    }

    private void deleteRepeatCustomerDetail(CustomerDetail customerDetail) {

        //todo: delete crm002 repeat customer detail by id.

        try {
            RepeatCustomerDetail repeatCustomerDetail = new RepeatCustomerDetail();
            repeatCustomerDetail.setId(customerDetail.getId());
            repeatCustomerDetail.setUserId(customerDetail.getUserId());
            repeatCustomerDetail.setNick(customerDetail.getNick());
            repeatCustomerDetail.setLastModifyTime(customerDetail.getLastModifyTime());
            repeatCustomerDetail.setSource(objectMapper.writeValueAsString(customerDetail));
            transferEntityDao.merge(repeatCustomerDetail);

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
