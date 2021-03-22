package com.interview.data;

import lombok.Data;

@Data
public class OrderBook {

    private Double quantity = (double) 0;
    private OrderKey orderKey = null;

    public static OrderBook getOrderBook(OrderVO orderVO) {
        OrderKey orderKey = OrderKey.getOrderKey(orderVO);
        OrderBook orderBook = new OrderBook();
        orderBook.setOrderKey(orderKey);

        return orderBook;
    }

    @Override
    public String toString() {
        return orderKey + " - " + quantity;

    }
}
