package com.darcytech.transfer.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by darcy on 2015/12/22.
 */
@Entity
public class ActionRecordIdMapping {

    private String oldId;

    @Id
    private Long id;

    public String getOldId() {
        return oldId;
    }

    public void setOldId(String oldId) {
        this.oldId = oldId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
