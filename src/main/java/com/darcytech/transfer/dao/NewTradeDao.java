package com.darcytech.transfer.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.darcytech.transfer.model.Order;
import com.darcytech.transfer.model.Trade;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by darcy on 2015/12/2.
 */
@Component
public class NewTradeDao {

    private static final Logger logger = LoggerFactory.getLogger(NewTradeDao.class);

    @Autowired
    private HttpClient httpClient;

    @Value("${es.test.addresses}")
    private String addresses;

    @Autowired
    private ObjectMapper objectMapper;

    public void bulkSave(List<Trade> trades) throws IOException {

        if (trades.isEmpty()) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();

        for (Trade trade : trades) {
            String customerId = trade.getUserId() + "_" + trade.getNick();
            stringBuilder.append("{ \"update\" : { \"_index\" : \"")
                    .append(trade.getUserId())
                    .append("\", ")
                    .append("\"_type\" : \"CustomerDetail\", ")
                    .append("\"_id\" : \"")
                    .append(StringEscapeUtils.escapeJava(customerId))
                    .append("\" } }")
                    .append("\n");

            if (trade.getOrders() == null) {
                logger.error("Trade has no orders, tid: " + trade.getId() + " oids: " + Arrays.toString(trade.getOids().toArray()));
            } else {
                for (Order order : trade.getOrders()) {
                    order.setCustomerId(null);
                    order.setTradeId(null);
                }
            }

            trade.setUserId(null);
            trade.setCustomerId(null);
            trade.setOids(null);
            trade.setNick(null);
            String valueAsString = objectMapper.writeValueAsString(trade);
            stringBuilder.append("{\"script\" : { \"file\":\"add-trade-to-customer\",\"params\" : {\"trade\" : ")
                    .append(valueAsString).append("}}, " + "\"upsert\": {\"id\": \"")
                    .append(customerId).append("\", \"trades\": [")
                    .append(valueAsString).append("]}}")
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
