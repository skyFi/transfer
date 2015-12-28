package com.darcytech.transfer.check;

import java.io.IOException;
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

import com.darcytech.transfer.model.CustomerDetail;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by darcy on 2015/12/25.
 */
@Component
public class CheckNewCustomerDao {

    private static final Logger logger = LoggerFactory.getLogger(CheckNewCustomerDao.class);

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${es.test.addresses}")
    private String addresses;

    public long count(Date start, Date end) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{\"query\":{\"range\":{\"lastModifyTime\":{\"gt\":")
                .append(start.getTime())
                .append(",\"lt\":")
                .append(end.getTime())
                .append("}}}}");

        HttpPost httpPost = new HttpPost("http://" + addresses + "/customer_*/CustomerDetail/_count?pretty");

        httpPost.setEntity(new StringEntity(stringBuilder.toString(), "UTF-8"));
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String response = EntityUtils.toString(httpResponse.getEntity());
        ObjectNode objectNode = objectMapper.readValue(response, ObjectNode.class);

        return objectNode.path("count").asLong();

    }

    public long getCustomer(Long userId, String buyerNick, Date start, Date end) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{\"query\":{\"bool\":{\"must\":[{\"range\":{\"lastModifyTime\":{\"gt\":" +
                start.getTime() +
                ",\"lt\":" +
                end.getTime()+
                "}}},{\"term\":{\"nick\":\"" +
                buyerNick +
                "\"}},{\"term\":{\"userId\":" +
                userId +
                "}}]}}}");

        HttpPost httpPost = new HttpPost("http://" + addresses + "/customer_*/CustomerDetail/_count?pretty");

        httpPost.setEntity(new StringEntity(stringBuilder.toString(), "UTF-8"));
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String response = EntityUtils.toString(httpResponse.getEntity());
        ObjectNode objectNode = objectMapper.readValue(response, ObjectNode.class);

        return objectNode.path("count").asLong();
    }
}
