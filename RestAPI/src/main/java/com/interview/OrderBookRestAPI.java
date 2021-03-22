package com.interview;

import com.interview.data.DataSideEnum;
import com.interview.data.OrderBook;
import com.interview.data.OrderBookCache;
import com.interview.data.OrderVO;
import com.interview.data.StatusEnum;
import com.interview.processor.IRestAPI;
import com.interview.util.SortByPrice;
import com.interview.workflow.Exchange;
import com.interview.workflow.WorkFlowController;
import com.interview.workflow.WorkFlowEnum;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderbook")
@Setter
public class OrderBookRestAPI implements IRestAPI {

  @Autowired
  private WorkFlowController workFlowController;

  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public String updateOrderBook(@RequestBody String data) throws Exception {
    evictEmptyData(OrderBookCache.getInstance());
    OrderVO orderVO = getOrderVO(data);
    Exchange exchange = new Exchange(orderVO);
    workFlowController.getWorkFlowActivities().get(WorkFlowEnum.UPDATE_CACHE).execute(exchange);
    OrderBook orderBook = OrderBookCache.getInstance().getCacheReference()
        .get(orderVO.getExchange())
        .get(orderVO.getSymbol()).get(OrderBook.OrderKey.getOrderKey(orderVO));

    return "Value " + (orderBook == null ? "NO_DATA" : orderBook.getQuantity() + "");
  }


  @RequestMapping(value = "/fetch", method = RequestMethod.GET)
  public ResponseEntity<String> fetchData(@RequestParam String exchange,
      @RequestParam String symbol) {
    evictEmptyData(OrderBookCache.getInstance());
    Map<String, Map<OrderBook.OrderKey, OrderBook>> exchangeMap = OrderBookCache.getInstance()
        .getCacheReference().get(exchange);
    if (exchangeMap == null) {
      return getResponseEntity404();
    }
    Map<OrderBook.OrderKey, OrderBook> orderKeyOrderBookMap = exchangeMap.get(symbol);
    if (orderKeyOrderBookMap == null) {
      return getResponseEntity404();
    }

    List<OrderBook> buyOrderBookStream = getListOfOrderBook(orderKeyOrderBookMap, DataSideEnum.BUY);
    List<OrderBook> sellOrderBookStream = getListOfOrderBook(orderKeyOrderBookMap,
        DataSideEnum.SELL);

    //Better sort while display rather then inserting into Cache as market updates are at high rate then display frequency.
    sortData(buyOrderBookStream, sellOrderBookStream);

    String book = "Value(Price-Qty) \n Buy -" + getPrintBook(buyOrderBookStream) + " \n Sell - "
        + getPrintBook(sellOrderBookStream);
    return new ResponseEntity(book, HttpStatus.OK);
  }


  private String getPrintBook(List<OrderBook> book) {
    return book.toString().replace(",", " |");
  }

  private List<OrderBook> getListOfOrderBook(
      Map<OrderBook.OrderKey, OrderBook> orderKeyOrderBookMap, DataSideEnum buy) {
    return orderKeyOrderBookMap.entrySet().stream().filter(e -> e.getKey()
        .getSide().equals(buy)).map(Map.Entry::getValue).collect(Collectors.toList());
  }

  private void sortData(List<OrderBook> buyOrderBookStream, List<OrderBook> sellOrderBookStream) {
    buyOrderBookStream.sort(Collections.reverseOrder(new SortByPrice()));
    sellOrderBookStream.sort(new SortByPrice());
  }

  private ResponseEntity getResponseEntity404() {
    return new ResponseEntity(HttpStatus.NOT_FOUND);
  }

  private OrderVO getOrderVO(String data) {
    String[] splitData = data.split(",");
    return OrderVO.builder()
        .timestamp(Long.parseLong(splitData[0]))
        .exchange(splitData[1])
        .symbol(splitData[2])
        .side(DataSideEnum.valueOf(splitData[3]))
        .price(Double.parseDouble(splitData[4]))
        .quantity(Double.parseDouble(splitData[5]))
        .status(StatusEnum.valueOf(splitData[6]))
        .type(splitData[7])
        .build();

  }

  private void evictEmptyData(OrderBookCache topOfBook) {
    topOfBook.getCacheReference().forEach((k, v) -> v.forEach((k1, v1) -> v1.entrySet()
        .removeIf((v3 -> v3.getValue().getQuantity().doubleValue() <= 0))));
  }
}
