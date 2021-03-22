package com.interview.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ApplicationPropertiesTest {

    private ApplicationProperties applicationProperties = new ApplicationProperties();


    @Test
    public void testPojo()
    {
        applicationProperties.setCsvFilePath("ABC");
        applicationProperties.setNoOfConsumerThread(100);
        applicationProperties.setNoOfProducerThread(200);

        Assertions.assertEquals("ABC", applicationProperties.getCsvFilePath());
        Assertions.assertEquals(100, applicationProperties.getNoOfConsumerThread());
        Assertions.assertEquals(200, applicationProperties.getNoOfProducerThread());
    }
}