package com.interview.workflow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.interview.util.Util.getExchange;

class ValidateDataTest {

    private final IProcess updateCache = new ValidateData();

    @Test
    public void testValidationForExchangeIsNull() throws Exception {
        Assertions.assertThrows(Exception.class, () -> updateCache.execute(null));
    }

    @Test
    public void testValidationForOrderIsNull() throws Exception {
        Assertions.assertThrows(Exception.class, () -> updateCache.execute(new Exchange(null)));
    }

    @Test
    public void testValidationAllGood() throws Exception {
        Exchange exchange = getExchange();
        updateCache.execute(exchange);
        Assertions.assertNotNull(exchange.getOrderVO());
    }
}