package com.interview.controller;

import com.interview.config.ApplicationProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ThreadControllerTest {

    private final ThreadController threadController = new ThreadController();
    private final ApplicationProperties applicationProperties = new ApplicationProperties();

    @BeforeEach
    public void init() {
        applicationProperties.setNoOfProducerThread(1);
        applicationProperties.setNoOfConsumerThread(1);
        threadController.setApplicationProperties(applicationProperties);
        threadController.init();
    }

    @Test
    public void testThreadsInitialization() {
        Assertions.assertNotNull(threadController.getConsumerExecutor());
        Assertions.assertNotNull(threadController.getProducerExecutor());
        Assertions.assertFalse(threadController.getConsumerExecutor().isTerminated());
        Assertions.assertFalse(threadController.getProducerExecutor().isTerminated());
    }

}