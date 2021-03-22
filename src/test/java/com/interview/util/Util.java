package com.interview.util;

import com.interview.data.OrderVO;
import com.interview.data.StatusEnum;
import com.interview.workflow.Exchange;

public class Util {

    private Util()
    {

    }

    public static Exchange getExchange() {
        OrderVO orderVO = new OrderVO();
        orderVO.setTimestamp(1001000000);
        orderVO.setExchange("TEST_EX");
        orderVO.setSymbol("TEST_SYM");
        orderVO.setPrice(1000.0);
        orderVO.setQuantity(2000.0);
        orderVO.setStatus(StatusEnum.NEW);
        orderVO.setType("EXECUTE");
        Exchange exchange = new Exchange(orderVO);
        return exchange;
    }
}
