package com.darcytech.transfer.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public void bulkSave(List<TradeRate> tradeRates) throws IOException {

        if (tradeRates.isEmpty()) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (TradeRate tradeRate : tradeRates) {
            stringBuilder.append("{ \"index\" : { \"_index\" : \"")
                    .append(tradeRate.getUserId())
                    .append("\", ")
                    .append("\"_type\" : \"TradeRate\", ")
                    .append("\"_id\" : \"")
                    .append(tradeRate.getCustomerId())
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
}
