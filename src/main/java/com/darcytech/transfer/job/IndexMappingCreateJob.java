package com.darcytech.transfer.job;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.dao.ElasticIndexMappingDao;
import com.darcytech.transfer.dao.ProdCustomerDao;
import com.darcytech.transfer.dao.TransferEntityDao;
import com.darcytech.transfer.model.ElasticIndexMapping;
import com.darcytech.transfer.util.MapSortUtil;

/**
 * Created by darcy on 2015/12/2.
 */
@Component
public class IndexMappingCreateJob {

    private static final Logger logger = LoggerFactory.getLogger(IndexMappingCreateJob.class);

    @Autowired
    private ProdCustomerDao prodCustomerDao;

    @Autowired
    private ElasticIndexMappingDao elasticIndexMappingDao;

    @Autowired
    private TransferEntityDao transferEntityDao;

    private Map<String, Long> newIndexDocCounts;

    public void createElasticIndexMapping() throws Exception {

        List<String> mappedUserList = elasticIndexMappingDao.getMappedUsers();
        int count = 0;
        Map<String, Long> userDocCounts = prodCustomerDao.getUserDocCounts();

        Map<String, Long> sortedDocCounts = MapSortUtil.orderByValue(userDocCounts, new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                return (int) (o2.getValue() - o1.getValue());
            }
        });

        for (Map.Entry<String, Long> entry : sortedDocCounts.entrySet()) {
            count = count + 1;
            if (!mappedUserList.contains(entry.getKey())) {
                String smallestIndex = findSmallestIndex();

                //保存用户的id与index的映射表
                ElasticIndexMapping elasticIndexMapping = new ElasticIndexMapping();
                elasticIndexMapping.setUserId(Long.valueOf(entry.getKey()));
                elasticIndexMapping.setCustomerIndex(smallestIndex);
                transferEntityDao.persist(elasticIndexMapping);

                newIndexDocCounts.put(smallestIndex, newIndexDocCounts.get(smallestIndex) + entry.getValue());
                logger.debug("user#" + entry.getKey() + ",distribution index: " + smallestIndex + " -> " + count + "/" + userDocCounts.size());
                if (count == userDocCounts.size()) {
                    logger.debug("index doc counts: " + String.valueOf(newIndexDocCounts));
                }
            }
        }
    }

    private String findSmallestIndex() throws IOException {

        if (newIndexDocCounts == null) {
            newIndexDocCounts = elasticIndexMappingDao.getIndexDocCounts();
        }

        Long smallestIndexDoc = Long.MAX_VALUE;
        String smallestIndexKey = "customer_0";
        for (Map.Entry<String, Long> entry : newIndexDocCounts.entrySet()) {
            if (smallestIndexDoc > entry.getValue()) {
                smallestIndexDoc = entry.getValue();
                smallestIndexKey = entry.getKey();
            }
        }

        return smallestIndexKey;
    }

}
