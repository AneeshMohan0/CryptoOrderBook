package com.interview.data;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.interview.util.Utility.getNewMap;

@Getter
public class OrderBookCache {

    private static final OrderBookCache INSTANCE = new OrderBookCache();

    //Exchange -> Instrument -> OrderBook (OrderBookKey, OrderBook)
    private final Map<String, Map<String, Map<OrderKey, OrderBook>>> cacheReference = getNewMap();

    private OrderBookCache() {

    }

    public static OrderBookCache getInstance() {
        return INSTANCE;
    }

}
