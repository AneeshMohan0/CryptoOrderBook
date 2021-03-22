package com.interview.data;

import com.interview.workflow.Exchange;
import com.interview.workflow.IProcess;
import com.interview.workflow.UpdateCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.interview.util.Util.getExchange;

class OrderBookCacheTest {


    @Test
    public void checkIfCacheDataDepth() throws Exception {
        OrderBookCache.getInstance().getCacheReference().clear();
        IProcess updateCache = new UpdateCache();
        Exchange exchange = getExchange();
        updateCache.execute(exchange);
        Assertions.assertNotNull(OrderBookCache.getInstance());
        Assertions.assertNotNull(exchange.getOrderVO());

    }
}