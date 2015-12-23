package com.darcytech.transfer.transfer;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.elasticsearch.search.SearchHits;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.darcytech.transfer.dao.EsScroller;

/**
 * Created by darcy on 2015/12/3.
 */
public abstract class AbstractTransferrer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void transferByDay(Date day) throws Exception {
        Date start = new DateTime(day).withTimeAtStartOfDay().toDate();
        Date end = new DateTime(day).plusDays(1).withTimeAtStartOfDay().toDate();
        EsScroller esScroll = getScroller(start, end);
        try {
            scrollAndTransfer(esScroll, new SimpleDateFormat("yyyy-MM-dd").format(day));
        } finally {
            //esScroll.close();
        }
    }

    public void transfer() throws Exception {
        EsScroller esScroll = getScroller(null, null);
        scrollAndTransfer(esScroll, "No last modification time transfer. ");
    }

    private void scrollAndTransfer(EsScroller esScroll, String day) throws Exception {

        int count = 0;
        while (true) {
            SearchHits searchHits = esScroll.next();
            count = count + searchHits.getHits().length;
            if (searchHits.getHits().length == 0) {
                break;
            }
            bulkTransfer(searchHits);
            logger.debug("Day: " + day + " progress: " + count + "/" + searchHits.getTotalHits());
        }
    }

    protected abstract void bulkTransfer(SearchHits searchHits) throws Exception;

    protected abstract EsScroller getScroller(Date start, Date end);

}
