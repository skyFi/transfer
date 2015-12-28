package com.darcytech.transfer.check;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.darcytech.transfer.enumeration.FailedDataType;

/**
 * Created by darcy on 2015/12/25.
 */
@Repository
public class FailedDataDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public long count(FailedDataType type, Date start, Date end) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String startTime = simpleDateFormat.format(start);
        String endTime = simpleDateFormat.format(end);

        String sql = "select count(*) from failed_data where `type` = ? AND transfer_time > ? AND transfer_time < ?";
       return jdbcTemplate.queryForObject(sql, Long.class, type.name(), startTime, endTime);
    }

    public long getFailedData(Long userId, String nick, FailedDataType type) {

        String sql = "select count(*) from failed_data where `type` = ? and user_id = ? and buyer_nick = ?";

        return jdbcTemplate.queryForObject(sql, Long.class, type.name(), userId, nick);

    }
}
