package com.interview.processor;

import com.interview.config.ApplicationProperties;
import com.interview.controller.ThreadController;
import com.interview.queue.BufferQueue;
import com.interview.workflow.Exchange;
import com.interview.workflow.UpdateCache;
import com.interview.workflow.ValidateData;
import com.interview.workflow.WorkFlowController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.interview.util.Util.getExchange;


class DataProcessorTest {

    private final OrderBookRestAPI orderBookRestAPI = new OrderBookRestAPI();
    private final ThreadController iProducerController = new ThreadController();
    private final BufferQueue queue = new BufferQueue();
    private final ApplicationProperties applicationProperties = new ApplicationProperties();
    private final WorkFlowController workFlowController = new WorkFlowController();
    private final DataProcessor dataProcessor = new DataProcessor();

    @BeforeEach
    public void init() {
        applicationProperties.setNoOfProducerThread(1);
        applicationProperties.setNoOfConsumerThread(1);
        applicationProperties.setCsvFilePath("src/main/resources/solidus_orderbook_challenge_data.csv");

        iProducerController.setApplicationProperties(applicationProperties);
        iProducerController.init();
        workFlowController.setValidateData(new ValidateData());
        workFlowController.setUpdateCache(new UpdateCache());
        workFlowController.init();

        dataProcessor.setConsumerController(iProducerController);
        dataProcessor.setBufferQueue(queue);
        dataProcessor.setWorkFlowController(workFlowController);
        dataProcessor.init();


    }

    @Test
    public void testDataWriteToQueue() throws InterruptedException {
        Exchange exchange = getExchange();


        Assertions.assertTrue(queue.getQueue().isEmpty());
        queue.getQueue().put(exchange.getOrderVO());
        new Thread(dataProcessor).start();
        Assertions.assertTrue(queue.getQueue().isEmpty());

    }
}