package com.darcytech.transfer.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by darcy on 2015/12/22.
 */
@Entity
public class ElasticIndexMapping extends BaseModel{

    private Long userId;

    private String customerIndex;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCustomerIndex() {
        return customerIndex;
    }

    public void setCustomerIndex(String customerIndex) {
        this.customerIndex = customerIndex;
    }
}
