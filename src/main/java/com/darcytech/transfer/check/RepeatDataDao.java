package com.darcytech.transfer.check;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by darcy on 2015/12/28.
 */
@Repository
public class RepeatDataDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public long count(Date start, Date end) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String startTime = simpleDateFormat.format(start);
        String endTime = simpleDateFormat.format(end);

        String sql = "select count(*) from repeat_customer_detail where last_modify_time > ? AND last_modify_time < ?";
        return jdbcTemplate.queryForObject(sql, Long.class, startTime, endTime);
    }
}
