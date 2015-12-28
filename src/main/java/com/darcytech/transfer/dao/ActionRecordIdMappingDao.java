package com.darcytech.transfer.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.darcytech.transfer.model.ActionRecordIdMapping;
import com.google.common.collect.Maps;

/**
 * User: dixi
 * Time: 2015/12/28 16:21
 */
@Repository
public class ActionRecordIdMappingDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Map<String, Long> getIdByOldIds(Set<String> actionRecordIds) {
        String sql = "select * from action_record_id_mapping where old_id in (:oids)";

        Map<String, Object> params = new HashMap<>();
        params.put("oids", actionRecordIds);

        List<ActionRecordIdMapping> mappings = namedParameterJdbcTemplate.query(
                sql, params, new BeanPropertyRowMapper<>(ActionRecordIdMapping.class));

        Map<String, Long> map = Maps.newHashMap();
        for (ActionRecordIdMapping mapping : mappings) {
            map.put(mapping.getOldId(), mapping.getId());
        }

        return map;
    }
}
