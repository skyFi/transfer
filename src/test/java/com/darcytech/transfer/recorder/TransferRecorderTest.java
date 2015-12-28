package com.darcytech.transfer.recorder;

import java.io.File;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.darcytech.transfer.test.BaseTest;

public class TransferRecorderTest extends BaseTest {

    @Autowired
    private TransferRecorder transferRecorder;

    @Test
    public void testReadFile() throws Exception {
        String fileName = "user-index.json";
        String file = transferRecorder.readFileAsString(fileName);
        Assert.assertNotNull(file);
    }

}