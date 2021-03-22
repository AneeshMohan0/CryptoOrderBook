package com.interview.controller;

import com.interview.config.ApplicationProperties;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Setter
public class ThreadController implements IProducerController, IConsumerController {

    @Autowired
    private ApplicationProperties applicationProperties;

    private ExecutorService consumerExecutor = null;
    private ExecutorService producerExecutor = null;

    public ExecutorService getProducerExecutor() {
        return producerExecutor;
    }
    public ExecutorService getConsumerExecutor() {
        return consumerExecutor;
    }

    @PostConstruct
    public void init() {
        consumerExecutor = Executors.newFixedThreadPool(applicationProperties.getNoOfConsumerThread());
        producerExecutor = Executors.newFixedThreadPool(applicationProperties.getNoOfProducerThread());
    }

}
