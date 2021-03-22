package com.interview.data;

import com.interview.util.Utility;
import lombok.Getter;

import java.util.Map;

@Getter
public class OrderBookCache {

    private static final OrderBookCache INSTANCE = new OrderBookCache();

    //Exchange -> Instrument -> OrderBook (OrderBookKey, OrderBook)
    private final Map<String, Map<String, Map<OrderKey, OrderBook>>> cacheReference = Utility.getNewMap();

    private OrderBookCache() {

    }

    public static OrderBookCache getInstance() {
        return INSTANCE;
    }

}
