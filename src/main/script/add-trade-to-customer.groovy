if (ctx._source.trades == null) {
    ctx._source.trades = new ArrayList();
};

def trades = ctx._source.trades;

if (trades.size() == 0) {
    trades.add(trade);
} else {
    boolean added = false;
    for (i = 0; i < trades.size(); i++) {
        if (trades[i].tid == trade.tid) {
            trades[i] = trade;
            added = true;
        }
    }
    if (!added) {
        trades.add(trade);
    }
}

