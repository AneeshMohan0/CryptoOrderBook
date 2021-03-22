package com.interview.cache.factory;

import com.interview.data.OrderBook;
import com.interview.data.OrderBook.OrderKey;
import java.util.Map;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CacheFactory {

  private final CacheType<String, Map<OrderKey, OrderBook>> exchange = new CacheType<>();
  private final CacheType<OrderKey, OrderBook> ticker = new CacheType<>();
  private final CacheType<String, Map<String, Map<OrderKey, OrderBook>>> cache = new CacheType<>();
}
