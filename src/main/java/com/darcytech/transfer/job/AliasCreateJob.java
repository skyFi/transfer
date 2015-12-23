package com.darcytech.transfer.job;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.dao.AliasDao;
import com.darcytech.transfer.dao.ProdCustomerDao;
import com.darcytech.transfer.dao.TransferEntityDao;
import com.darcytech.transfer.enumeration.AliasType;
import com.darcytech.transfer.model.ElasticIndexMapping;
import com.darcytech.transfer.util.MapSortUtil;

/**
 * Created by darcy on 2015/12/2.
 */
@Component
public class AliasCreateJob {

    private static final Logger logger = LoggerFactory.getLogger(AliasCreateJob.class);

    @Autowired
    private ProdCustomerDao prodCustomerDao;

    @Autowired
    private AliasDao aliasDao;

    @Autowired
    private TransferEntityDao transferEntityDao;

    @Value("${tradeRate.index.line.start}")
    private String tradeRateIndexStartLine;

    @Value("${tradeRate.index.line.end}")
    private String tradeRateIndexEndLine;

    private Map<String, Long> newIndexDocCounts;

    public void createAliases() throws Exception {

        List<String> newAliases = aliasDao.getNewAliases();
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
            if (!newAliases.contains(entry.getKey())) {
                String smallestIndex = findSmallestIndex();
                //创建Customer的aliases
                aliasDao.createNewAliases(smallestIndex, entry.getKey(), AliasType.CUSTOMER);

                //保存用户的id与index的映射表
                ElasticIndexMapping elasticIndexMapping = new ElasticIndexMapping();
                elasticIndexMapping.setId(Long.valueOf(entry.getKey()));
                elasticIndexMapping.setCustomerIndex(smallestIndex);
                transferEntityDao.save(elasticIndexMapping);

                newIndexDocCounts.put(smallestIndex, newIndexDocCounts.get(smallestIndex) + entry.getValue());
                logger.debug("user#" + entry.getKey() + ",aliasIndex: " + smallestIndex + " -> " + count + "/" + userDocCounts.size());
                if (count == userDocCounts.size()) {
                    logger.debug("index doc counts: " + String.valueOf(newIndexDocCounts));
                }
            }
        }
    }

    private String findSmallestIndex() throws IOException {

        if (newIndexDocCounts == null) {
            newIndexDocCounts = aliasDao.getIndexDocCounts();
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
