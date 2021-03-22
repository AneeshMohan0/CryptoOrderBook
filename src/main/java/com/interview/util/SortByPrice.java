package com.interview.util;

import com.interview.data.OrderBook;

import java.util.Comparator;

public class SortByPrice implements Comparator<OrderBook> {
    @Override
    public int compare(OrderBook orderBook1, OrderBook orderBook2) {
        return Double.compare(orderBook1.getOrderKey().getPrice(), orderBook2.getOrderKey().getPrice());

    }
}
