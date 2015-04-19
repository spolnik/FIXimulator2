package com.wordpress.nprogramming.instruments.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Arrays;

public class Instrument {

    @NotEmpty
    private final String ticker;
    @Length(min = 9, max = 9, message = "length must be 9")
    private final String cusip;
    @Length(min = 7, max = 7, message = "length must be 7")
    private final String sedol;
    @NotEmpty
    private final String name;
    @NotEmpty
    private final String ric;
    @NotEmpty
    private final String price;

    @JsonCreator
    public Instrument(
            @JsonProperty("ticker") String ticker,
            @JsonProperty("sedol") String sedol,
            @JsonProperty("name") String name,
            @JsonProperty("ric") String ric,
            @JsonProperty("cusip") String cusip,
            @JsonProperty("price") String price
    ) {
        this.ticker = ticker;
        this.cusip = cusip;
        this.sedol = sedol;
        this.name = name;
        this.ric = ric;
        this.price = price;
    }

    @JsonProperty
    public String getTicker() {
        return ticker;
    }

    @JsonProperty
    public String getCusip() {
        return cusip;
    }

    @JsonProperty
    public String getSedol() {
        return sedol;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getRIC() {
        return ric;
    }

    @JsonProperty
    public String getPrice() {
        return price;
    }

    public boolean canBeIdentifiedBy(String id) {
        return Arrays.asList(
                getName(),
                getTicker(),
                getCusip(),
                getRIC(),
                getSedol()
        ).contains(id);
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "ticker='" + ticker + '\'' +
                ", cusip='" + cusip + '\'' +
                ", sedol='" + sedol + '\'' +
                ", name='" + name + '\'' +
                ", ric='" + ric + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    public static Instrument empty() {
        return new Instrument("", "", "", "", "", "");
    }
}