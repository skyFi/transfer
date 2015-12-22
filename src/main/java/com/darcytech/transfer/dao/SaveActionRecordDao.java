package com.darcytech.transfer.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.darcytech.transfer.model.ActionRecord;
import com.darcytech.transfer.model.ActionRecordIdMapping;

/**
 * Created by darcy on 2015/12/21.
 */
@Repository
@Transactional
public class SaveActionRecordDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(List<ActionRecord> actionRecords) {
        for (ActionRecord actionRecord : actionRecords) {

            if (!checkActionRecordMapping(actionRecord)) {
                ActionRecordIdMapping actionRecordIdMapping = new ActionRecordIdMapping();
                actionRecordIdMapping.setOldId(actionRecord.getOldId());

                ActionRecord newActionRecord = entityManager.merge(actionRecord);

                actionRecordIdMapping.setId(newActionRecord.getId());
                entityManager.merge(actionRecordIdMapping);
            }
        }
    }

    private boolean checkActionRecordMapping(ActionRecord actionRecord) {

        String oldId = actionRecord.getOldId();
        String sql = "SELECT count(*) FROM action_record_id_mapping where old_id = ?  ";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, oldId);
        return result >= 1;
    }
}
