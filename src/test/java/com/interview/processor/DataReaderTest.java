package com.interview.processor;

import com.interview.config.ApplicationProperties;
import com.interview.controller.IProducerController;
import com.interview.controller.ThreadController;
import com.interview.data.OrderBookCache;
import com.interview.data.OrderVO;
import com.interview.data.StatusEnum;
import com.interview.queue.BufferQueue;
import com.interview.workflow.UpdateCache;
import com.interview.workflow.ValidateData;
import com.interview.workflow.WorkFlowController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class DataReaderTest {

    private DataReader dataReader = new DataReader();

    private final OrderBookRestAPI orderBookRestAPI = new OrderBookRestAPI();
    private final ThreadController iProducerController = new ThreadController();
    private final BufferQueue queue = new BufferQueue();
    private final ApplicationProperties applicationProperties = new ApplicationProperties();

    @BeforeEach
    public void init() {
        applicationProperties.setNoOfProducerThread(1);
        applicationProperties.setNoOfConsumerThread(1);
        applicationProperties.setCsvFilePath("src/main/resources/solidus_orderbook_challenge_data.csv");

        iProducerController.setApplicationProperties(applicationProperties);
        iProducerController.init();

        dataReader.setProducerController(iProducerController );
        dataReader.setQueue(queue );
        dataReader.setApplicationProperties(applicationProperties );


    }

    @Test
    public void testDataRead() throws InterruptedException {
        applicationProperties.setCsvFilePath("src/main/resources/solidus_orderbook_challenge_data.csv");
        dataReader.writeDataToQueues();

        Assertions.assertEquals(34, queue.getQueue().size());
        OrderVO orderVO = queue.getQueue().take();

        Assertions.assertEquals("GMAX", orderVO.getExchange());
        Assertions.assertEquals(315.7, orderVO.getPrice());
        Assertions.assertEquals(StatusEnum.NEW, orderVO.getStatus());
        Assertions.assertEquals(52.78, orderVO.getQuantity());
        Assertions.assertEquals(33, queue.getQueue().size());
    }


    @Test
    public void testExceptionWhileReadingData() throws InterruptedException {
        applicationProperties.setCsvFilePath("test");
        dataReader.writeDataToQueues();
        //Capture verification needed here.
    }

    @Test
    public void testExceptionWhileReadingFromQueue() throws InterruptedException {
        applicationProperties.setCsvFilePath("src/main/resources/solidus_orderbook_challenge_data.csv");
        dataReader.getProducerController().getProducerExecutor().shutdownNow();
        dataReader.writeDataToQueues();
        //Capture verification needed here.
    }

}