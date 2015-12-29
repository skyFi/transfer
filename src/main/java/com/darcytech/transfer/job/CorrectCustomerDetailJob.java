package com.darcytech.transfer.job;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.dao.CorrectRepeatCustomerDetailDao;

/**
 * Created by darcy on 2015/12/28.
 */
@Component
public class CorrectCustomerDetailJob {

    @Autowired
    private CorrectRepeatCustomerDetailDao correctRepeatCustomerDetailDao;

    public void doCorrectCustomerDetail() throws IOException {
        correctRepeatCustomerDetailDao.correctRepeatData();
    }
}
