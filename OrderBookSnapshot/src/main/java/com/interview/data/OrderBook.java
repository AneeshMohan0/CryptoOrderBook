package com.interview.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.concurrent.atomic.DoubleAccumulator;

@Data
public class OrderBook {

    //Write Frequency is more then read Frequency.
    private DoubleAccumulator quantity = new DoubleAccumulator(Double::sum, 0d);
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

    @Data
    @EqualsAndHashCode
    public static class OrderKey {


        private Double price;
        private DataSideEnum side;

        public static OrderKey getOrderKey(OrderVO orderVO) {
            OrderKey orderKey = new OrderKey();
            orderKey.setPrice(orderVO.getPrice());
            orderKey.setSide(orderVO.getSide());

            return orderKey;
        }

        @Override
        public String toString() {
            return price.toString();
        }

    }
}
