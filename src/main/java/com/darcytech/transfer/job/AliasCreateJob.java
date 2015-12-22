package com.darcytech.transfer.job;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.elasticsearch.search.lookup.FieldLookup;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.dao.AliasDao;
import com.darcytech.transfer.dao.ProdCustomerDao;
import com.darcytech.transfer.enumeration.AliasType;
import com.darcytech.transfer.recorder.TransferRecorder;
import com.darcytech.transfer.util.MapSortUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
    private TransferRecorder transferRecorder;

    private File userIndexRecorderFile = new File("user-index.json");

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

        JSONObject userIndex = transferRecorder.readRecordAsJson(userIndexRecorderFile);
        for (Map.Entry<String, Long> entry : sortedDocCounts.entrySet()) {
            count = count + 1;
            if (!newAliases.contains(entry.getKey())) {
                String smallestIndex = findSmallestIndex();
                //创建Customer的aliases
                aliasDao.createNewAliases(smallestIndex, entry.getKey(), AliasType.CUSTOMER);

                userIndex.put(entry.getKey(), smallestIndex);

                newIndexDocCounts.put(smallestIndex, newIndexDocCounts.get(smallestIndex) + entry.getValue());
                logger.debug("user#" + entry.getKey() + ",aliasIndex: " + smallestIndex + " -> " + count + "/" + userDocCounts.size());
                if (count == userDocCounts.size()) {
                    logger.debug("index doc counts: " + String.valueOf(newIndexDocCounts));
                }
            }
        }

        //记录用户所在的index，保存TradeRate查询用。
        if (userIndex.length() != 0) {
            transferRecorder.writeRecordAsJson(String.valueOf(userIndex), userIndexRecorderFile);
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
