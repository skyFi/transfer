package com.darcytech.transfer.transfer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.dao.EsScroller;
import com.darcytech.transfer.dao.NewCustomerDao;
import com.darcytech.transfer.dao.ProdCustomerDao;
import com.darcytech.transfer.model.CustomerDetail;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by darcy on 2015/12/2.
 */
@Component
public class CustomerTransferrer extends AbstractTransferrer {

    @Autowired
    private ProdCustomerDao prodCustomerDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NewCustomerDao newCustomerDao;

    @Override
    protected void bulkTransfer(SearchHits searchHits) throws IOException {
        List<CustomerDetail> customerDetails = new ArrayList<>();
        Set<String> existsUsers = prodCustomerDao.getExistsUsers();
        for (SearchHit customerSearchHit : searchHits) {
            CustomerDetail customerDetail = objectMapper.readValue(customerSearchHit.getSourceAsString(), CustomerDetail.class);

            if (existsUsers.contains(String.valueOf(customerDetail.getUserId()))) {
                customerDetail.setId(customerDetail.getUserId() + "_" + customerDetail.getNick());
                customerDetails.add(customerDetail);
            }
        }
        newCustomerDao.bulkSave(customerDetails);
    }

    @Override
    protected EsScroller getScroller(Date start, Date end) {
        return prodCustomerDao.prepareScrollByDate(start, end);
    }

}
