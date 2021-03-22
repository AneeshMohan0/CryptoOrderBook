package com.interview.workflow;

import com.interview.config.ApplicationProperties;
import com.interview.data.OrderBook;
import com.interview.data.OrderBookCache;
import com.interview.data.OrderKey;
import com.interview.data.StatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static com.interview.util.Utility.getNewMap;

@Component
@Slf4j
public class UpdateCache implements IProcess {

    @Autowired
    private ApplicationProperties applicationProperties;
    public void execute(Exchange data) {

        OrderBookCache topOfBook = OrderBookCache.getInstance();

        initIfAbsent(data, topOfBook);

        topOfBook.getCacheReference().get(data.getOrderVO().getExchange())
                .get(data.getOrderVO().getSymbol()).compute(OrderKey.getOrderKey(data.getOrderVO()), (orderBookeKey, orderBook) -> {

            orderBook = checkAndInitializeOrderBook(data, orderBook);

            updateCache(data, orderBook);

            return orderBook;
        });


        evictEmptyData(topOfBook);
    }

    private void updateCache(Exchange data, OrderBook orderBook) {
        if (addToOrderBook(data)) {
            orderBook.setQuantity(orderBook.getQuantity() + data.getOrderVO().getQuantity());
        } else if (removeFromOrderBook(data)) {
            orderBook.setQuantity(orderBook.getQuantity() - data.getOrderVO().getQuantity());
        }
    }

    private void evictEmptyData(OrderBookCache topOfBook) {
        topOfBook.getCacheReference().forEach((k, v) -> v.forEach((k1, v1) -> v1.entrySet().removeIf((v3 -> v3.getValue().getQuantity() <= 0))));
    }

    private void initIfAbsent(Exchange data, OrderBookCache topOfBook) {
        topOfBook.getCacheReference().putIfAbsent(data.getOrderVO().getExchange(), getNewMap());
        topOfBook.getCacheReference().get(data.getOrderVO().getExchange())
                .putIfAbsent(data.getOrderVO().getSymbol(), getNewMap());
    }

    private boolean removeFromOrderBook(Exchange data) {
        return StatusEnum.CANCEL.equals(data.getOrderVO().getStatus()) || StatusEnum.EXECUTE.equals(data.getOrderVO().getStatus());
    }

    private boolean addToOrderBook(Exchange data) {
        return StatusEnum.NEW.equals(data.getOrderVO().getStatus());
    }

    private OrderBook checkAndInitializeOrderBook(Exchange data, OrderBook orderBook) {
        return orderBook == null ? OrderBook.getOrderBook(data.getOrderVO()) : orderBook;
    }

}
