package com.interview.processor;

import com.interview.data.OrderBookCache;
import com.interview.workflow.UpdateCache;
import com.interview.workflow.ValidateData;
import com.interview.workflow.WorkFlowController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class OrderBookRestAPITest {

    private final WorkFlowController workFlowController = new WorkFlowController();
    private final OrderBookRestAPI orderBookRestAPI = new OrderBookRestAPI();

    @BeforeEach
    public void init() {
        workFlowController.setValidateData(new ValidateData());
        workFlowController.setUpdateCache(new UpdateCache());
        workFlowController.init();
        orderBookRestAPI.setWorkFlowController(workFlowController);
        OrderBookCache.getInstance().getCacheReference().clear();
    }


    @Test
    public void testUpdateCache() throws Exception {
        String orderBook = orderBookRestAPI.updateOrderBook("1500717600563,GMAX,ETH/USD,BUY,315.7,52.78,NEW,LIMIT");
        Assertions.assertNotNull(orderBook);
        Assertions.assertTrue(orderBook.contains("52.78"));
    }

    @Test
    public void testFetchFromCacheWhenDataExist() throws Exception {
        orderBookRestAPI.updateOrderBook("1500717600563,GMAX,ETH/USD,BUY,315.7,52.78,NEW,LIMIT");
        orderBookRestAPI.updateOrderBook("1500717600563,GMAX,ETH/USD,BUY,315.7,52.78,NEW,LIMIT");
        orderBookRestAPI.updateOrderBook("1500717600563,GMAX,ETH/USD,SELL,315.7,52.78,NEW,LIMIT");
        ResponseEntity<String> gmax = orderBookRestAPI.fetchData("GMAX", "ETH/USD");
        Assertions.assertNotNull(gmax);
        Assertions.assertEquals(HttpStatus.OK, gmax.getStatusCode());
        Assertions.assertTrue(gmax.getBody().contains("315.7"));

    }

    @Test
    public void testFetchFromCacheSymbolDataDoesNotExist() throws Exception {

        orderBookRestAPI.updateOrderBook("1500717600563,GMAX,ETH/USD,BUY,315.7,52.78,NEW,LIMIT");
        ResponseEntity<String> gmax = orderBookRestAPI.fetchData("GMAX", "WRONG");
        Assertions.assertNotNull(gmax);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, gmax.getStatusCode());

    }

    @Test
    public void testFetchFromCacheExchangeDataDoesNotExist() throws Exception {
        orderBookRestAPI.updateOrderBook("1500717600563,GMAX,ETH/USD,BUY,315.7,52.78,NEW,LIMIT");
        ResponseEntity<String> gmax = orderBookRestAPI.fetchData("WRONG", "WRONG");
        Assertions.assertNotNull(gmax);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, gmax.getStatusCode());

    }

}