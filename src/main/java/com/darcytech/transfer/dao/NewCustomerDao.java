package com.darcytech.transfer.dao;

import java.io.IOException;
import java.util.ArrayList;
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

import com.darcytech.transfer.model.CustomerDetail;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by darcy on 2015/12/1.
 */
@Component
public class NewCustomerDao {

    private static final Logger logger = LoggerFactory.getLogger(NewCustomerDao.class);

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${es.test.addresses}")
    private String addresses;

    public void bulkSave(List<CustomerDetail> customerDetails) throws IOException {

        if (customerDetails.isEmpty()) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();

        for (CustomerDetail customerDetail : customerDetails) {
            stringBuilder.append("{ \"update\" : { \"_index\" : \"")
                    .append(customerDetail.getUserId())
                    .append("\", ")
                    .append("\"_type\" : \"CustomerDetail\", ")
                    .append("\"_id\" : \"")
                    .append(StringEscapeUtils.escapeJava(customerDetail.getId()))
                    .append("\" } }")
                    .append("\n");

            stringBuilder.append("{ \"doc\" : ")
                    .append(objectMapper.writeValueAsString(customerDetail))
                    .append(", \"doc_as_upsert\" : true}")
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