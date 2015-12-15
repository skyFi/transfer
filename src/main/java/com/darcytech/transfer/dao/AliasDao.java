package com.darcytech.transfer.dao;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.enumeration.AliasType;
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

    @Value("${customer.index.count}")
    private Long customerIndexCount;

    @Value("${tradeRate.index.line.start}")
    private String tradeRateIndexStartLine;

    @Value("${tradeRate.index.line.end}")
    private String tradeRateIndexEndLine;

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;

    public List<String> getNewAliases() throws Exception {

        HttpGet httpGet = new HttpGet("http://" + newAddresses + "/_aliases");
        HttpResponse httpResponse = httpClient.execute(httpGet);
        String response = EntityUtils.toString(httpResponse.getEntity());

        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            throw new IOException(response);
        }

        ObjectNode objectNode = objectMapper.readValue(response, ObjectNode.class);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<String> newAliases = new ArrayList<>();
        for (int i = 0; i < customerIndexCount; i++) {
            JsonNode customerAliasesNode = objectNode.path("customer_" + i).path("aliases");
            Iterator<String> customerFieldName = customerAliasesNode.fieldNames();
            while (customerFieldName.hasNext()) {
                String customerAlias = customerFieldName.next();
                newAliases.add(customerAlias);
            }

            Date startTime = simpleDateFormat.parse(tradeRateIndexStartLine);
            Date endTime = simpleDateFormat.parse(tradeRateIndexEndLine);
            while (startTime.before(endTime)){

                JsonNode tradeRateAliasesNode = objectNode.path("traderate_" + new SimpleDateFormat("yyyyMM").format(startTime) + "_" + i).path("aliases");
                Iterator<String> tradeRateFieldNames = tradeRateAliasesNode.fieldNames();
                while (tradeRateFieldNames.hasNext()) {
                    String tradeRateAlias = tradeRateFieldNames.next();
                    newAliases.add(tradeRateAlias);
                }
                startTime = new DateTime(startTime).plusMonths(1).toDate();
            }
        }

        return newAliases;
    }

    public void createNewAliases(String index, String alias, AliasType type) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"actions\": [");

        if (type == AliasType.CUSTOMER) {
            stringBuilder.append("{\"add\":{\"index\":\"" + index + "\"," +
                    "\"alias\":\"" + alias + "\"," +
                    "\"filter\":{\"term\":{\"userId\":\"" + alias + "\"}}}}");
        }

        if (type == AliasType.TRADERATE) {
            String tradeRateUserId = alias.substring(0, alias.indexOf("_"));
            stringBuilder.append("{\"add\":{\"index\":\"" + index + "\"," +
                    "\"alias\":\"" + alias + "\"," +
                    "\"filter\":{\"term\":{\"userId\":\"" + tradeRateUserId + "\"}}}}");
        }

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

        Map<String, Long> indexDocCounts = new HashMap<>();
        for (int i = 0; i < customerIndexCount; i++) {
            String indexName = "customer_" + i;
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
