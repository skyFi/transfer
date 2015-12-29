package com.darcytech.transfer.check;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

import com.darcytech.transfer.model.CustomerDetail;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by darcy on 2015/12/25.
 */
@Component
public class CheckNewTradeDao {


    private static final Logger logger = LoggerFactory.getLogger(CheckNewTradeDao.class);

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${es.test.addresses}")
    private String addresses;

    public long count(Date start, Date end) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{\"query\":{\"nested\":{\"path\":\"trades\",\"query\":{\"range\":{\"trades.lastModifyTime\":{\"gt\":")
                .append(start.getTime())
                .append(",\"lt\":")
                .append(end.getTime())
                .append("}}}}}}");

        HttpPost httpPost = new HttpPost("http://" + addresses + "/customer_*/CustomerDetail/_count?pretty");

        httpPost.setEntity(new StringEntity(stringBuilder.toString(), "UTF-8"));
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String response = EntityUtils.toString(httpResponse.getEntity());
        ObjectNode objectNode = objectMapper.readValue(response, ObjectNode.class);

        List<String> errorString = new ArrayList<>();
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

        return objectNode.path("count").asLong();

    }


    public List<CustomerDetail> getDirtyData() throws Exception{

        StringBuilder stringBuilder = new StringBuilder();
        List<CustomerDetail> customerDetails = new ArrayList<>();

        stringBuilder.append("{\"query\":{\"bool\":{\"must\":[{\"query\":{\"nested\":{\"path\":\"trades\",\"query\":{\"match_all\":{}}}}},{\"query\":{\"missing\":{\"field\":\"nick\"}}}]}}}");

        HttpPost httpPost = new HttpPost("http://" + addresses + "/customer_*/CustomerDetail/_search?pretty&size=10000");

        httpPost.setEntity(new StringEntity(stringBuilder.toString(), "UTF-8"));
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String response = EntityUtils.toString(httpResponse.getEntity());
        ObjectNode objectNode = objectMapper.readValue(response, ObjectNode.class);

        List<String> errorString = new ArrayList<>();
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

        return customerDetails;
    }

}
