package com.interview.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "yaml")
@PropertySource(ignoreResourceNotFound = true, value = "file:OrderBookSnapshot/src/main/resources/application.yml", factory = YamlPropertySourceFactory.class)
@Data
@ToString
public class ApplicationProperties {
    private String name;
    private int noOfProducerThread;
    private int noOfConsumerThread;
    private String csvFilePath;
}
