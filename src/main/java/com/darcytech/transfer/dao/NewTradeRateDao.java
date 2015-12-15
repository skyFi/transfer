package com.darcytech.transfer.dao;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.model.TradeRate;
import com.darcytech.transfer.recorder.TransferRecorder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by darcy on 2015/12/3.
 */
@Component
public class NewTradeRateDao {

    private static final Logger logger = LoggerFactory.getLogger(NewTradeRateDao.class);

    @Autowired
    private HttpClient httpClient;

    @Value("${es.test.addresses}")
    private String addresses;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransferRecorder transferRecorder;

    private List<String> fileList;

    public void bulkSave(List<TradeRate> tradeRates) throws Exception {

        if (tradeRates.isEmpty()) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (TradeRate tradeRate : tradeRates) {
            String index = getTradeRateIndexByMouthAndUserId(String.valueOf(tradeRate.getUserId()), tradeRate.getCreatedTime());
            if (index == null) {
                throw new Exception("TradeRate didn't find alias: " + String.valueOf(tradeRate.getUserId()) + "_" + new SimpleDateFormat("yyyyMM").format(tradeRate.getCreatedTime()));
            }
            stringBuilder.append("{ \"index\" : { \"_index\" : \"")
                    .append(index)
                    .append("\", ")
                    .append("\"_type\" : \"TradeRate\", ")
                    .append("\"_id\" : \"")
                    .append(StringEscapeUtils.escapeJava(tradeRate.getCustomerId()))
                    .append("\" } }")
                    .append("\n");

            stringBuilder.append(objectMapper.writeValueAsString(tradeRate))
                    .append("\n");
        }

        HttpPost httpPost = new HttpPost("http://" + addresses + "/_bulk");

        httpPost.setEntity(new StringEntity(stringBuilder.toString(), "UTF-8"));
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String response = EntityUtils.toString(httpResponse.getEntity());

        List<String> errorString = new ArrayList<>();
        ObjectNode objectNode = objectMapper.readValue(response, ObjectNode.class);
        if (objectNode.path("errors").asBoolean()) {
            for (JsonNode item : objectNode.path("items")) {
                if (item.path("update").path("status").asInt() != 200 && item.path("update").path("status").asInt() != 201) {
                    errorString.add(String.valueOf(item));
                }
            }
        }
        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            logger.debug(stringBuilder.toString());
            throw new IOException(response);
        }

        if (objectNode.path("errors").asBoolean()) {
            throw new IOException(String.valueOf(errorString));
        }

    }

    private String getTradeRateIndexByMouthAndUserId(String userId, Date createdTime) throws Exception {
        if (fileList == null) {
            File file = new File("trade-rate-aliases.rcd");
            fileList = transferRecorder.readRecord(file);
        }
        String tradeRateAlias = null;
        for (String alias : fileList) {
            if (alias.contains(userId) && alias.contains(new SimpleDateFormat("yyyyMM").format(createdTime))) {
                tradeRateAlias = alias;
            }
        }
        return tradeRateAlias;
    }

    public String getIndexByUserId(String userId) throws IOException {
        HttpPost httpPost = new HttpPost("http://" + addresses + "/" + userId + "/CustomerDetail/_index");
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String response = EntityUtils.toString(httpResponse.getEntity());
        ObjectNode objectNode = objectMapper.readValue(response, ObjectNode.class);

        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            throw new IOException(response);
        }
        return objectNode.get("_index").toString();
    }
}
