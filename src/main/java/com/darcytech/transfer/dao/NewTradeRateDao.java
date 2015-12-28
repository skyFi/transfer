package com.darcytech.transfer.dao;

import java.io.IOException;
import java.text.SimpleDateFormat;
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

import com.darcytech.transfer.enumeration.FailedDataType;
import com.darcytech.transfer.enumeration.FailedReason;
import com.darcytech.transfer.model.ElasticIndexMapping;
import com.darcytech.transfer.model.FailedData;
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

    private List<ElasticIndexMapping> indexMappings;

    @Autowired
    private TransferEntityDao transferEntityDao;

    @Autowired
    private ElasticIndexMappingDao elasticIndexMappingDao;

    public void bulkSave(List<TradeRate> tradeRates) throws Exception {

        if (tradeRates.isEmpty()) {
            return;
        }
        if (indexMappings == null) {
            indexMappings = elasticIndexMappingDao.getIndexMappingList();
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (TradeRate tradeRate : tradeRates) {

            String userIndex = null;
            String tradeRateIndex = null;
            for (ElasticIndexMapping customerIndex : indexMappings) {
                if (customerIndex.getUserId().equals(tradeRate.getUserId())) {
                    userIndex = customerIndex.getCustomerIndex();
                }
            }

            if (userIndex != null) {
                tradeRateIndex = "traderate_" + new SimpleDateFormat("yyyyMM").format(tradeRate.getCreatedTime()) + userIndex.substring(userIndex.indexOf("_"));
            } else {
                FailedData failedData = new FailedData();

                failedData.setProdId(tradeRate.getId());
                failedData.setUserId(tradeRate.getUserId());
                failedData.setBuyerNick(tradeRate.getNick());
                failedData.setType(FailedDataType.TRADERATE);
                failedData.setReason(FailedReason.CANT_FIND_USERID);
                failedData.setTransferTime(tradeRate.getCreatedTime());

                transferEntityDao.persist(failedData);

                throw new Exception("can`t find this customer(" + tradeRate.getUserId() + ") index for traderate.");
            }
            stringBuilder.append("{ \"index\" : { \"_index\" : \"")
                    .append(tradeRateIndex)
                    .append("\", ")
                    .append("\"_type\" : \"TradeRate\", ")
                    .append("\"_id\" : \"")
                    .append(StringEscapeUtils.escapeJava(tradeRate.getCustomerId()))
                    .append("\" } }")
                    .append("\n");

            tradeRate.setCustomerId(null);

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
