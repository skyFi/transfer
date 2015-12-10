package com.darcytech.transfer.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by darcy on 2015/12/2.
 */
@Component
public class AliasDao {

    @Value("${es.test.addresses}")
    private String newAddresses;

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;

    public List<String> getNewAliases() throws IOException {

        HttpGet httpGet = new HttpGet("http://" + newAddresses + "/_aliases");
        HttpResponse httpResponse = httpClient.execute(httpGet);
        String response = EntityUtils.toString(httpResponse.getEntity());

        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            throw new IOException(response);
        }

        ObjectNode objectNode = objectMapper.readValue(response, ObjectNode.class);

        List<String> newAliases = new ArrayList<>();
        for (int i = 0; i < objectNode.size(); i++) {
            JsonNode aliasesNode = objectNode.path("index_" + i).path("aliases");
            Iterator<String> fieldNames = aliasesNode.fieldNames();
            while (fieldNames.hasNext()) {
                String alias = fieldNames.next();
                newAliases.add(alias);
            }
        }

        return newAliases;
    }

    public void createNewAliases(String index, String aliase) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"actions\": [");

        stringBuilder.append("{\"add\":{\"index\":\"" + index + "\"," +
                "\"alias\":\"" + aliase + "\"," +
                "\"filter\":{\"term\":{\"userId\":\"" + aliase + "\"}}}}");

        stringBuilder.append("]}");

        HttpPost httpPost = new HttpPost("http://" + newAddresses + "/_aliases");
        httpPost.setEntity(new StringEntity(stringBuilder.toString(), "UTF-8"));

        HttpResponse httpResponse = httpClient.execute(httpPost);
        String response = EntityUtils.toString(httpResponse.getEntity());

        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            throw new IOException(response);
        }
    }

    public Map<String, Long> getIndexDocCounts() throws IOException {
        HttpGet httpGet = new HttpGet("http://" + newAddresses + "/_aliases");
        HttpResponse httpResponse = httpClient.execute(httpGet);
        String response = EntityUtils.toString(httpResponse.getEntity());

        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            throw new IOException(response);
        }

        ObjectNode objectNode = objectMapper.readValue(response, ObjectNode.class);
        Map<String, Long> indexDocCounts = new HashMap<>();
        for (int i = 0; i < objectNode.size(); i++) {
            String indexName = "index_" + i;
            Long indexDocsCount = countIndexDocs(indexName);
            indexDocCounts.put(indexName, indexDocsCount);
        }
        return indexDocCounts;
    }

    private Long countIndexDocs(String index) throws IOException {
        HttpGet httpGet = new HttpGet("http://" + newAddresses + "/" + index + "/CustomerDetail/_count");
        HttpResponse httpResponse = httpClient.execute(httpGet);
        String response = EntityUtils.toString(httpResponse.getEntity());

        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            throw new IOException(response);
        }

        ObjectNode objectNode = objectMapper.readValue(response, ObjectNode.class);
        return objectNode.path("count").asLong();
    }

}
