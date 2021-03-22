package com.interview.processor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


public interface IRestAPI {

    String updateOrderBook(@RequestBody String data) throws Exception;
    ResponseEntity<String> fetchData(@RequestParam String exchange, @RequestParam String symbol);
}
