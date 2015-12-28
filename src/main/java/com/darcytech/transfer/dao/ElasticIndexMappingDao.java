package com.darcytech.transfer.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.model.ElasticIndexMapping;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by darcy on 2015/12/2.
 */
@Component
public class ElasticIndexMappingDao {

    @Value("${es.test.addresses}")
    private String newAddresses;

    @Value("${customer.index.count}")
    private Long customerIndexCount;

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    public List<String> getMappedUsers() throws Exception {

        List<String> mappedUsers = new ArrayList<>();

        List<ElasticIndexMapping> indexMappingList = getIndexMappingList();

        for (ElasticIndexMapping mapping : indexMappingList) {
            mappedUsers.add(String.valueOf(mapping.getUserId()));
        }

        return mappedUsers;
    }

    public List<ElasticIndexMapping> getIndexMappingList() {
        StringBuilder hql = new StringBuilder();
        hql.append(" from ElasticIndexMapping");
        Query q = entityManager.createQuery(hql.toString());
        return q.getResultList();
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
