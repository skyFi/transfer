package com.darcytech.transfer.job;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.dao.AliasDao;
import com.darcytech.transfer.dao.ProdCustomerDao;
import com.darcytech.transfer.enumeration.AliasType;
import com.darcytech.transfer.recorder.TransferRecorder;

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

    @Value("${tradeRate.index.line.start}")
    private String tradeRateIndexStartLine;

    @Value("${tradeRate.index.line.end}")
    private String tradeRateIndexEndLine;

    private Map<String, Long> newIndexDocCounts;

    public void createAliases() throws Exception {

        List<String> newAliases = aliasDao.getNewAliases();
        int count = 0;
        Map<String, Long> userDocCounts = prodCustomerDao.getUserDocCounts();
        File customerIndexFile = new File("trade-rate-aliases.rcd");
        for (Map.Entry<String, Long> entry : userDocCounts.entrySet()) {
            count = count + 1;
            if (!newAliases.contains(entry.getKey())) {
                String smallestIndex = findSmallestIndex();
                //创建Customer的aliases
                aliasDao.createNewAliases(smallestIndex, entry.getKey(), AliasType.CUSTOMER);

                //创建TradeRate的aliases
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date startTime = simpleDateFormat.parse(tradeRateIndexStartLine);
                Date endTime = simpleDateFormat.parse(tradeRateIndexEndLine);
                while (startTime.before(endTime)){

                    /**
                     * TradeRate index ang alias example:
                     * index-> traderate_201512_5
                     * alias-> 54520057_201512_5
                     */
                    String index = "traderate_" + new SimpleDateFormat("yyyyMM").format(startTime) + smallestIndex.substring(smallestIndex.indexOf("_"));
                    String alias = entry.getKey() + "_" + new SimpleDateFormat("yyyyMM").format(startTime) + smallestIndex.substring(smallestIndex.indexOf("_"));
                    aliasDao.createNewAliases(index, alias, AliasType.TRADERATE);

                    /**
                     * 记录TradeRate的aliases,保存TradeRate的时候用
                     * */
                    transferRecorder.writeRecord(alias, customerIndexFile);

                    startTime = new DateTime(startTime).plusMonths(1).toDate();
                }



                newIndexDocCounts.put(smallestIndex, newIndexDocCounts.get(smallestIndex) + entry.getValue());
                logger.debug("user#" + entry.getKey() + ",aliasIndex: " + smallestIndex + " -> " + count + "/" + userDocCounts.size());
//                logger.debug("index doc counts: " + String.valueOf(newIndexDocCounts));
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
