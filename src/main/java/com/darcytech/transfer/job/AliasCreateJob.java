package com.darcytech.transfer.job;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.dao.AliasDao;
import com.darcytech.transfer.dao.ProdCustomerDao;

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

    private Map<String, Long> newIndexDocCounts;

    public void createAliases() throws IOException {

        List<String> newAliases = aliasDao.getNewAliases();
        int count = 0;
        Map<String, Long> userDocCounts = prodCustomerDao.getUserDocCounts();
        for (Map.Entry<String, Long> entry : userDocCounts.entrySet()) {
            count = count + 1;
            if (!newAliases.contains(entry.getKey())) {
                String smallestIndex = findSmallestIndex();
                aliasDao.createNewAliases(smallestIndex, entry.getKey());
                newIndexDocCounts.put(smallestIndex, newIndexDocCounts.get(smallestIndex) + entry.getValue());
                logger.debug("user#" + entry.getKey() + ",aliasIndex: " + smallestIndex + " -> " + count + "/" + userDocCounts.size());
            }
        }
    }

    private String findSmallestIndex() throws IOException {

        if (newIndexDocCounts == null) {
            newIndexDocCounts = aliasDao.getIndexDocCounts();
        }

        Long smallestIndexDoc = Long.MAX_VALUE;
        String smallestIndexKey = "index_0";
        for (Map.Entry<String, Long> entry : newIndexDocCounts.entrySet()) {
            if (smallestIndexDoc > entry.getValue()) {
                smallestIndexDoc = entry.getValue();
                smallestIndexKey = entry.getKey();
            }
        }

        return smallestIndexKey;
    }

}
