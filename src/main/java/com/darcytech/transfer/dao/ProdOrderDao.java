package com.darcytech.transfer.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.model.Order;
import com.darcytech.transfer.model.Trade;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by darcy on 2015/12/1.
 */
@Component
public class ProdOrderDao {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Client esClient;

    public List<Order> multiGetOrdersByTrades(List<Trade> trades) throws IOException {

        MultiGetRequestBuilder searchRequest = esClient.prepareMultiGet();

        for (Trade trade : trades) {
            for (Long oid : trade.getOids()) {
                MultiGetRequest.Item item = new MultiGetRequest.Item(trade.getUserId().toString(), Order.class.getSimpleName(), oid.toString());
                item.routing(trade.getCustomerId());
                searchRequest.add(item);
            }
        }

        MultiGetItemResponse[] responses = searchRequest.execute().actionGet().getResponses();

        List<Order> orders = new ArrayList<>();
        for (MultiGetItemResponse multiGetItemResponse : responses) {
            GetResponse response = multiGetItemResponse.getResponse();
            if (multiGetItemResponse.getFailure() == null) {
                if (response.isExists()) {
                    orders.add(objectMapper.readValue(response.getSourceAsBytes(), Order.class));
                }
            } else {
                throw new IOException(multiGetItemResponse.getFailure().getMessage());
            }
        }
        return orders;
    }
}
