package com.interview.data;

import com.interview.cache.factory.CacheFactory;
import com.interview.data.OrderBook.OrderKey;
import java.util.Map;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
public class OrderBookCache {

  private static final OrderBookCache INSTANCE = new OrderBookCache();

  @Autowired
  private CacheFactory cacheFactory;


  //Exchange -> Instrument -> OrderBook (OrderBookKey, OrderBook)
  private final Map<String, Map<String, Map<OrderKey, OrderBook>>> cacheReference = cacheFactory
      .getCache().getNewMap();

  private OrderBookCache() {

  }

  public static OrderBookCache getInstance() {
    return INSTANCE;
  }

}
