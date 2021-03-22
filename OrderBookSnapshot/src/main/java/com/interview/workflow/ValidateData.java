package com.interview.workflow;

import org.springframework.stereotype.Component;

@Component
public class ValidateData implements IProcess {

    //Need more detailed validation.
    public void execute(Exchange data) throws Exception {
        if (data == null) {
            throw new Exception("Exchange Data is null");
        }

        if (data.getOrderVO() == null) {
            throw new Exception("Order Data is null");
        }
    }
}
