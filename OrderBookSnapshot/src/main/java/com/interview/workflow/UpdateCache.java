package com.interview.workflow;

import static com.interview.data.OrderBook.OrderKey.getOrderKey;

import com.interview.cache.factory.CacheFactory;
import com.interview.data.OrderBook;
import com.interview.data.OrderBookCache;
import com.interview.data.StatusEnum;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdateCache implements IProcess {

  private final BiConsumer<OrderBook, Double> accumulate = this::accumulate;
  private final BiFunction<Exchange, OrderBook, OrderBook> checkAndInitializeOrderBook = this::checkAndInitializeOrderBook;
  private final Predicate<Exchange> addToOrderBook = this::addToOrderBook;
  private final Predicate<Exchange> removeFromOrderBook = this::removeFromOrderBook;
  private final BiFunction<Exchange, OrderBook, Double> updateCache = this::getEventType;
  private final BiConsumer<Exchange, OrderBookCache> initIfAbsent = this::initIfAbsent;

  @Autowired
  private CacheFactory cacheFactory;


  public void execute(@NonNull Exchange data) {

    OrderBookCache topOfBook = OrderBookCache.getInstance();
    initIfAbsent.accept(data, topOfBook);

    topOfBook
        .getCacheReference()
        .get(data.getOrderVO().getExchange())
        .get(data.getOrderVO().getSymbol())
        .compute(getOrderKey(data.getOrderVO()), (orderBookKey, orderBook) -> {

          OrderBook apply = checkAndInitializeOrderBook.apply(data, orderBook);
          accumulate.accept(apply, updateCache.apply(data, apply));
          return apply;
        });


  }

  private Double getEventType(Exchange data, OrderBook orderBook) {
    if (addToOrderBook.and(removeFromOrderBook).test(data)) {
      return -data.getOrderVO().getQuantity();
    }
    if (addToOrderBook.and(removeFromOrderBook.negate()).test(data)) {
      return data.getOrderVO().getQuantity();
    }
    return 0D;
  }

  private void accumulate(OrderBook orderBook, Double value) {
    orderBook.getQuantity().accumulate(value);
  }


  private void initIfAbsent(Exchange data, OrderBookCache topOfBook) {
    topOfBook.getCacheReference()
        .putIfAbsent(data.getOrderVO().getExchange(), cacheFactory.getExchange().getNewMap());
    topOfBook.getCacheReference().get(data.getOrderVO().getExchange())
        .putIfAbsent(data.getOrderVO().getSymbol(), cacheFactory.getTicker().getNewMap());
  }

  private boolean removeFromOrderBook(Exchange data) {
    return StatusEnum.CANCEL.equals(data.getOrderVO().getStatus()) || StatusEnum.EXECUTE
        .equals(data.getOrderVO().getStatus());
  }

  private boolean addToOrderBook(Exchange data) {
    return StatusEnum.NEW.equals(data.getOrderVO().getStatus());
  }

  private OrderBook checkAndInitializeOrderBook(Exchange data, OrderBook orderBook) {
    return orderBook == null ? OrderBook.getOrderBook(data.getOrderVO()) : orderBook;
  }

}
