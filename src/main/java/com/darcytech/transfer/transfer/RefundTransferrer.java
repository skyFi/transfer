package com.darcytech.transfer.transfer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.dao.EsScroller;
import com.darcytech.transfer.dao.ProdRefundDao;
import com.darcytech.transfer.dao.TransferEntityDao;
import com.darcytech.transfer.model.Refund;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by darcy on 2015/12/22.
 */
@Component
public class RefundTransferrer extends AbstractTransferrer {

    @Autowired
    private ProdRefundDao prodRefundDao;

    @Autowired
    private TransferEntityDao transferEntityDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void bulkTransfer(SearchHits searchHits) throws Exception {

        List<Refund> refunds = new ArrayList<>();
        for (SearchHit searchHit : searchHits) {
            Refund refund = objectMapper.readValue(searchHit.getSourceAsString(), Refund.class);

            if (searchHit.getSource().get("desc") != null && StringUtils.isNotEmpty(searchHit.getSource().get("desc").toString())) {
                refund.setDescription(searchHit.getSource().get("desc").toString());
            }
            refunds.add(refund);

        }
        transferEntityDao.save(refunds);
    }

    @Override
    protected EsScroller getScroller(Date start, Date end) {
        return prodRefundDao.prepareScrollByDate(start, end);
    }
}
