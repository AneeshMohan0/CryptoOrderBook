package com.interview.processor;


import com.interview.config.ApplicationProperties;
import com.interview.controller.IProducerController;
import com.interview.data.OrderVO;
import com.interview.queue.BufferQueue;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Data
public class DataReader {

    @Autowired
    private IProducerController producerController;

    @Autowired
    private BufferQueue queue;

    @Autowired
    private ApplicationProperties applicationProperties;

    @PostConstruct
    public void init() {
        producerController.getProducerExecutor().submit(() -> writeDataToQueues());
    }

    public void writeDataToQueues() {
        //We need to put this in infinite loop for reading stream of data.

        List<OrderVO> beans = getParseCSV();

        beans.forEach(data -> {
            log.info("Input Data {} ", data.toString());
            try {
                queue.getQueue().put(data); // blocks if MAX_BLOCKS_IN_QUEUE_UNTIL_BLOCK
            } catch (InterruptedException e) {
                log.error("Error while executing WorkFlow", e);
                e.printStackTrace();
            }
        });


    }

    private List getParseCSV() {
        log.info("Reading Data from CSV");
        try {
            return new CsvToBeanBuilder(new FileReader(applicationProperties.getCsvFilePath()))
                    .withType(OrderVO.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            log.error("Error while executing WorkFlow", e);
            e.printStackTrace();
        }
        return new ArrayList();
    }
}
