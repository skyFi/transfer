package com.darcytech.transfer.transfer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.dao.EsScroller;
import com.darcytech.transfer.dao.NewCustomerDao;
import com.darcytech.transfer.dao.ProdCustomerDao;
import com.darcytech.transfer.dao.TransferEntityDao;
import com.darcytech.transfer.enumeration.FailedDataType;
import com.darcytech.transfer.enumeration.FailedReason;
import com.darcytech.transfer.model.CustomerDetail;
import com.darcytech.transfer.model.FailedData;
import com.darcytech.transfer.model.RepeatCustomer;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by darcy on 2015/12/2.
 */
@Component
public class CustomerTransferrer extends AbstractTransferrer {

    private final Logger logger = LoggerFactory.getLogger(CustomerTransferrer.class);

    @Autowired
    private ProdCustomerDao prodCustomerDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NewCustomerDao newCustomerDao;

    @Autowired
    private TransferEntityDao transferEntityDao;

    @Override
    protected void bulkTransfer(SearchHits searchHits) throws Exception {
        List<CustomerDetail> customerDetails = new ArrayList<>();
        Set<String> existsUsers = prodCustomerDao.getExistsUsers();
        for (SearchHit customerSearchHit : searchHits) {
            CustomerDetail customerDetail = objectMapper.readValue(customerSearchHit.getSourceAsString(), CustomerDetail.class);
            if (existsUsers.contains(String.valueOf(customerDetail.getUserId()))) {

                customerDetail.setId(customerDetail.getUserId() + "_" + customerDetail.getNick());
                customerDetails.add(customerDetail);
            } else {
                FailedData failedData = new FailedData();

                failedData.setProdId(customerDetail.getId());
                failedData.setUserId(customerDetail.getUserId());
                failedData.setBuyerNick(customerDetail.getNick());
                failedData.setType(FailedDataType.CUSTOMER);
                failedData.setReason(FailedReason.EXPIRED);
                failedData.setTransferTime(customerDetail.getLastModifyTime());

                transferEntityDao.persist(failedData);
            }
        }
        newCustomerDao.bulkSave(customerDetails);
    }

    @Override
    protected EsScroller getScroller(Date start, Date end) {
        return prodCustomerDao.prepareScrollByDate(start, end);
    }

}
