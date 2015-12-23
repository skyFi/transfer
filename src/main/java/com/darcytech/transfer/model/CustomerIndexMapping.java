package com.darcytech.transfer.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by darcy on 2015/12/22.
 */
@Entity
public class CustomerIndexMapping {

    @Id
    private Long id;

    private String customerIndex;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerIndex() {
        return customerIndex;
    }

    public void setCustomerIndex(String customerIndex) {
        this.customerIndex = customerIndex;
    }
}
