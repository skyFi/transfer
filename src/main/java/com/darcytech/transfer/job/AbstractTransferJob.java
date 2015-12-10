package com.darcytech.transfer.job;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.recorder.TransferRecorder;

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

    @Autowired
    private TransferRecorder transferRecorder;

    public void doTransfer() throws Exception {
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
                        File recordFile = getRecordFile();
                        transferRecorder.writeRecord(transferDay, recordFile);

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

    protected abstract File getRecordFile() throws IOException;

    protected abstract void transferByDay(Date day) throws IOException;

    protected abstract BlockingQueue<Integer> prepareTokenQueue();

    private List<Date> prepareTransferDays() throws Exception {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Date> days = new ArrayList<>();
        Set<String> hasTransferredDays = new HashSet<>();
        File file = getRecordFile();
        List<String> fileList = transferRecorder.readRecord(file);
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
