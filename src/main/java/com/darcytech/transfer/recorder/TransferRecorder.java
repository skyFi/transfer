package com.darcytech.transfer.recorder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
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

    public synchronized void writeRecordAsJson(String record, File recordFile) throws Exception {
        IOUtils.write(record, new FileOutputStream(recordFile, false));
    }

    public JSONObject readRecordAsJson(File recordFile) throws Exception {
        if (recordFile.exists()) {
            String fileName = recordFile.getName();
            String jsonData = readFileAsString(fileName);
            return new JSONObject(jsonData);
        } else {
            return new JSONObject();
        }
    }

    public String readFileAsString(String filename) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
