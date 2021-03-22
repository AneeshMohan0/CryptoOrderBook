package com.interview.data;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
/**
 * Parse from CSV and map it to attribute.
 */
public class OrderVO {

    @CsvBindByName(column = "timestamp")
    private long timestamp;

    @CsvBindByName(column = "exchange")
    private String exchange;

    @CsvBindByName(column = "symbol")
    private String symbol;

    @CsvBindByName(column = "side")
    private DataSideEnum side;

    @CsvBindByName(column = "price")
    private double price;

    @CsvBindByName(column = "quantity")
    private double quantity;

    @CsvBindByName(column = "status")
    private StatusEnum status;

    @CsvBindByName(column = "type")
    private String type;

}
