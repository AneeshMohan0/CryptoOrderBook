package com.interview.util;

import com.interview.data.OrderVO;
import com.interview.data.StatusEnum;
import com.interview.workflow.Exchange;

public class Util {

    private Util() {

    }

    public static Exchange getExchange() {
        OrderVO orderVO = OrderVO.builder()
                .timestamp(1001000000)
                .exchange("TEST_EX")
                .symbol("TEST_SYM")
                .price(1000.0)
                .quantity(2000.0)
                .status(StatusEnum.NEW)
                .type("EXECUTE")
                .build();
        Exchange exchange = new Exchange(orderVO);
        return exchange;
    }
}
