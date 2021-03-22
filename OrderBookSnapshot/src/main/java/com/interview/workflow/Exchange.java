package com.interview.workflow;

import com.interview.data.OrderVO;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class Exchange {

    private final OrderVO orderVO;
    private HashMap<String, String> metaData= new HashMap<>();


    public Exchange(OrderVO orderVO) {
        this.orderVO = orderVO;
    }
}
