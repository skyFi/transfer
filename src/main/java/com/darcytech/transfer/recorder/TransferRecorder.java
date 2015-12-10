package com.darcytech.transfer.recorder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

/**
 * Created by darcy on 2015/12/2.
 */
@Component
public class TransferRecorder {

    public synchronized void writeRecord(String record, File recordFile) throws IOException {
        IOUtils.write(record + "\n", new FileOutputStream(recordFile, true));
    }

    public List<String> readRecord(File recordFile) throws IOException {
        if (recordFile.exists()) {
            return IOUtils.readLines(new FileInputStream(recordFile));
        } else {
            return null;
        }
    }

}
