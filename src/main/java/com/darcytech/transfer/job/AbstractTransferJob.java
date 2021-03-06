package com.darcytech.transfer.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Created by darcy on 2015/12/3.
 */
@Component
public abstract class AbstractTransferJob {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${transfer.line.start}")
    private String startLine;

    @Value("${transfer.line.end}")
    private String endLine;

    @Resource(name = "transferExecutor")
    private TaskExecutor transferExecutor;

    public void doTransfer() throws Exception {
        if (StringUtils.isNotEmpty(endLine)) {
            List<Date> dateList = prepareTransferDays();

            final Queue<Date> dateQueue = new ConcurrentLinkedQueue<>(dateList);
            final BlockingQueue<Integer> tokens = prepareTokenQueue();
            final CountDownLatch countDownLatch = new CountDownLatch(dateList.size());

            while (!dateQueue.isEmpty()) {
                final Date day = dateQueue.poll();
                final Integer token = tokens.take();
                transferExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            transferByDay(day);
                            String transferDay = new SimpleDateFormat("yyyy-MM-dd").format(day);
                            saveTransferRecord(transferDay);

                            logger.debug("transfer complete, day: " + transferDay);
                        } catch (Exception e) {
                            logger.error("Transfer data error, day = " + new SimpleDateFormat("yyyy-MM-dd").format(day), e);
                        } finally {
                            try {
                                tokens.put(token);
                                countDownLatch.countDown();
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                });
            }

            countDownLatch.await();
        }

        if (StringUtils.isEmpty(endLine)) {
            transferByDay(null);

            logger.debug("all no time data transfer complete.");
        }

    }

    protected abstract void saveTransferRecord(String transferDay) throws Exception;

    protected abstract void transferByDay(Date day) throws Exception;

    protected abstract BlockingQueue<Integer> prepareTokenQueue();

    protected abstract List<String> getTransferDays();

    private List<Date> prepareTransferDays() throws Exception {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Date> days = new ArrayList<>();
        Set<String> hasTransferredDays = new HashSet<>();
        List<String> fileList = getTransferDays();
        if (fileList != null) {
            hasTransferredDays.addAll(fileList);
        }

        Date startTime = simpleDateFormat.parse(startLine);
        Date endTime = simpleDateFormat.parse(endLine);

        //最后一天的数据不迁移
        while (startTime.before(endTime)) {
            if (!hasTransferredDays.contains(simpleDateFormat.format(startTime))) {
                days.add(startTime);
            }
            startTime = new DateTime(startTime).plusDays(1).toDate();
        }

        return days;
    }
}
