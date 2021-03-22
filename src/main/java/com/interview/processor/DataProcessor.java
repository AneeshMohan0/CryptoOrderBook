package com.interview.processor;

import com.interview.controller.IConsumerController;
import com.interview.data.OrderVO;
import com.interview.queue.BufferQueue;
import com.interview.workflow.Exchange;
import com.interview.workflow.WorkFlowController;
import com.interview.workflow.WorkFlowEnum;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
@Data
@Slf4j
public class DataProcessor implements Runnable {

    @Autowired
    private BufferQueue bufferQueue;

    @Autowired
    private IConsumerController consumerController;

    @Autowired
    private WorkFlowController workFlowController;

    @PostConstruct
    public void init() {
        consumerController.getConsumerExecutor().submit(this);
    }

    @Override
    public void run() {

        while (true) {
            OrderVO data = readDataFromQueue(); // blocks until there is a block of data
            processDataWithWorkFlowActivities(data);
        }

    }

    private OrderVO readDataFromQueue() {
        try {
            return bufferQueue.getQueue().take();
        } catch (InterruptedException e) {
            log.error("Error while Reading Data From Queue", e);
        }
        return null;
    }

    private void processDataWithWorkFlowActivities(OrderVO orderVO) {
        final Exchange exchange = new Exchange(orderVO);
        Arrays.stream(WorkFlowEnum.values()).forEach(wf -> {
            try {
                workFlowController.getWorkFlowActivities().get(wf).execute(exchange);
            } catch (Exception e) {
                log.error("Error while executing WorkFlow", e);
            }
        });
    }
}
