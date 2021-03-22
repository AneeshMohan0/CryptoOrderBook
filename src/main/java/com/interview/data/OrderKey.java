package com.interview.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
public class OrderKey {

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
