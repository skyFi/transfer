package com.darcytech.transfer.check;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by darcy on 2015/12/25.
 */
@Component
public abstract class AbstractCheckJob {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${transfer.line.start}")
    private String transferLineStart;

    @Value("${transfer.line.end}")
    private String transferLineEnd;

    @Value("${test.times}")
    private int testTimes;

    @Autowired
    private CheckProdCustomerDao checkProdCustomerDao;

    @Autowired
    private RepeatDataDao repeatDataDao;

    public void test() {

        try {
            if (testTimes == 0) {
                testAllDay();
            } else {
                testRandomDay();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    protected abstract long getNewCount(Date start, Date end) throws IOException;

    protected abstract long getProdCount(Date start, Date end);

    protected abstract long getFailedCount(Date start, Date end);

    protected abstract long getTotalCount(String transferDay);

    private void testRandomDay() throws Exception{

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        long leastData = Long.MAX_VALUE;
        Date leastStartTime = null;
        Date leastEndTime = null;

        for (int i = 0; i < testTimes; i++) {
            Date start = new DateTime(getRandomTime(simpleDateFormat.parse(transferLineStart), simpleDateFormat.parse(transferLineEnd))).withTimeAtStartOfDay().toDate();
            Date end = new DateTime(getRandomTime(simpleDateFormat.parse(transferLineStart), simpleDateFormat.parse(transferLineEnd))).withTimeAtStartOfDay().toDate();

            if (end.before(start)) {
                Date temp = start;
                start = end;
                end = temp;
            }

            long newCount = getNewCount(start, end);
            long prodCount = getProdCount(start, end);
            long failedCount = getFailedCount(start, end);
            long missing = prodCount - newCount - failedCount;
            long repeatCount = repeatDataDao.count(start, end);

            if ( missing > 0 && leastData > newCount) {
                leastData = newCount;
                leastStartTime = start;
                leastEndTime = end;
            }

            if (missing < 0) {
                long escapeCount = getTotalCount(simpleDateFormat.format(start)) - prodCount;
                logger.debug("{} ~ {}. missing: {}, escape: {}, repeat: {}, prod: {}, new: {}. failed: {}.",
                        simpleDateFormat.format(start), simpleDateFormat.format(end), missing, escapeCount, repeatCount, prodCount, newCount, failedCount);
            } else {
                logger.debug("{} ~ {}. missing: {}, repeat: {}, prod: {}, new: {}. failed: {}.",
                        simpleDateFormat.format(start), simpleDateFormat.format(end), missing, repeatCount, prodCount, newCount, failedCount);
            }
        }

        if (leastStartTime != null) {
            logger.debug("start randomly checking data : {}~{}", simpleDateFormat.format(leastStartTime), simpleDateFormat.format(leastEndTime));
            checkProdCustomerDao.showMissedCustomer(leastStartTime, leastEndTime);
        }
    }

    private void testAllDay() throws Exception {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = simpleDateFormat.parse(transferLineStart);
        Date endTime = simpleDateFormat.parse(transferLineEnd);

        long leastData = Long.MAX_VALUE;
        Date leastStartTime = null;
        Date leastEndTime = null;

        while (start.before(endTime)) {

            Date end = new DateTime(start).plusDays(1).toDate();
            long newCount = getNewCount(start, end);
            long prodCount = getProdCount(start, end);
            long failedCount = getFailedCount(start, end);
            long missing = prodCount - newCount - failedCount;

            if ( missing > 0 && leastData > newCount) {
                leastData = newCount;
                leastStartTime = start;
                leastEndTime = end;
            }

            if (missing < 0) {
                long escapeCount = getTotalCount(simpleDateFormat.format(start)) - prodCount;
                logger.debug("{} ~ {}. missing: {}, escape: {}  prod: {}, new: {}. failed: {}.",
                        simpleDateFormat.format(start), simpleDateFormat.format(end), missing, escapeCount, prodCount, newCount, failedCount);
            } else {
                logger.debug("{} ~ {}. missing: {}, prod: {}, new: {}. failed: {}.",
                        simpleDateFormat.format(start), simpleDateFormat.format(end), missing, prodCount, newCount, failedCount);
            }

            start = new DateTime(start).plusDays(1).toDate();
        }

        if (leastStartTime != null) {
            logger.debug("start all checking data : {}~{}", simpleDateFormat.format(leastStartTime), simpleDateFormat.format(leastEndTime));
            checkProdCustomerDao.showMissedCustomer(leastStartTime, leastEndTime);
        }
    }

    private Date getRandomTime(Date startTime, Date endTime) {

        long rangeBegin = startTime.getTime();
        long rangeEnd = endTime.getTime();

        long diff = rangeEnd - rangeBegin + 1;
        return new DateTime(rangeBegin + (long) (Math.random() * diff)).withTimeAtStartOfDay().toDate();
    }
}
