package com.interview.controller;

import java.util.concurrent.ExecutorService;

public interface IProducerController {
    ExecutorService getProducerExecutor();
}
