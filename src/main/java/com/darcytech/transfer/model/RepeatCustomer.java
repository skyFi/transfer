package com.darcytech.transfer.model;

import java.util.Date;

import javax.persistence.Entity;

/**
 * Created by darcy on 2015/12/28.
 */
@Entity
public class RepeatCustomer extends BaseModel{

    private Long userId;

    private String nick;

    private Date sourceDay;

    private Date targetDay;

}
