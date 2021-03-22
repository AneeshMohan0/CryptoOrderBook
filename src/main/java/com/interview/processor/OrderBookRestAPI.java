package com.interview.processor;

import com.interview.data.*;
import com.interview.util.SortByPrice;
import com.interview.workflow.Exchange;
import com.interview.workflow.WorkFlowController;
import com.interview.workflow.WorkFlowEnum;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orderbook")
@Setter
@Component
public class OrderBookRestAPI {

    @Autowired
    private WorkFlowController workFlowController;

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateOrderBook(@RequestBody String data) throws Exception {
        OrderVO orderVO = getOrderVO(data);
        Exchange exchange = new Exchange(orderVO);
        workFlowController.getWorkFlowActivities().get(WorkFlowEnum.UPDATE_CACHE).execute(exchange);
        OrderBook orderBook = OrderBookCache.getInstance().getCacheReference().get(orderVO.getExchange())
                .get(orderVO.getSymbol()).get(OrderKey.getOrderKey(orderVO));

        return "Value " + (orderBook == null ? "NO_DATA" : orderBook.getQuantity() + "");
    }


    @RequestMapping(value = "/fetch", method = RequestMethod.GET)
    public ResponseEntity<String> fetchData(@RequestParam String exchange, @RequestParam String symbol) {

        Map<String, Map<OrderKey, OrderBook>> exchangeMap = OrderBookCache.getInstance().getCacheReference().get(exchange);
        if (exchangeMap == null) {
            return getResponseEntity404();
        }
        Map<OrderKey, OrderBook> orderKeyOrderBookMap = exchangeMap.get(symbol);
        if (orderKeyOrderBookMap == null) {
            return getResponseEntity404();
        }

        List<OrderBook> buyOrderBookStream = getListOfOrderBook(orderKeyOrderBookMap, DataSideEnum.BUY);
        List<OrderBook> sellOrderBookStream = getListOfOrderBook(orderKeyOrderBookMap, DataSideEnum.SELL);

        //Better sort while display rather then inserting into Cache as market updates are at high rate then display frequency.
        sortData(buyOrderBookStream, sellOrderBookStream);

        String book = "Value(Price-Qty) \n Buy -" + getPrintBook(buyOrderBookStream) + " \n Sell - " + getPrintBook(sellOrderBookStream);
        return new ResponseEntity(book, HttpStatus.OK);
    }


    private String getPrintBook(List<OrderBook> book) {
        return book.toString().replace(",", " |");
    }

    private List<OrderBook> getListOfOrderBook(Map<OrderKey, OrderBook> orderKeyOrderBookMap, DataSideEnum buy) {
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

    //TODO need to find better way to map this.
    private OrderVO getOrderVO(String data) {
        OrderVO orderVO = new OrderVO();
        String[] splitData = data.split(",");

        orderVO.setTimestamp(Long.parseLong(splitData[0]));
        orderVO.setExchange(splitData[1]);
        orderVO.setSymbol(splitData[2]);
        orderVO.setSide(DataSideEnum.valueOf(splitData[3]));
        orderVO.setPrice(Double.parseDouble(splitData[4]));
        orderVO.setQuantity(Double.parseDouble(splitData[5]));
        orderVO.setStatus(StatusEnum.valueOf(splitData[6]));
        orderVO.setType(splitData[7]);

        return orderVO;
    }

}
