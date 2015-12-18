package com.darcytech.transfer.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

/**
 * User: dixi
 * Time: 2015/12/18 15:46
 */
@Repository
public class CustomerDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<Long, String> findCustomerByIds(Set<Long> customerIds) {
        String ids = Joiner.on(",").join(customerIds);

        String sql = "select id,taobao_nick from customer where id in (" + ids + ")";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        Map<Long, String> customerNickMap = Maps.newHashMap();
        for (Map<String, Object> map : list) {
            customerNickMap.put((Long) map.get("id"), map.get("taobao_nick").toString());
        }

        return customerNickMap;
    }

}
