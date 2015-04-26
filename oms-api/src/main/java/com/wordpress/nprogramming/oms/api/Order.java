package com.wordpress.nprogramming.oms.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Order implements Cloneable, ItemWithId {

    private static final String UNKNOWN = "<UNKNOWN>";

    private static final Map<Character, String> orderTypeMappings = new HashMap<>();

    static {
        orderTypeMappings.put('1', "Market");
        orderTypeMappings.put('2', "Limit");
        orderTypeMappings.put('3', "Stop");
        orderTypeMappings.put('4', "Stop limit");
        orderTypeMappings.put('5', "Market on close");
        orderTypeMappings.put('6', "With or without");
        orderTypeMappings.put('7', "Limit or better");
        orderTypeMappings.put('8', "Limit with or without");
        orderTypeMappings.put('9', "On basis");
        orderTypeMappings.put('A', "On close");
        orderTypeMappings.put('B', "Limit on close");
        orderTypeMappings.put('C', "Forex - Market");
        orderTypeMappings.put('D', "Previously quoted");
        orderTypeMappings.put('E', "Previously indicated");
        orderTypeMappings.put('F', "Forex - Limit");
        orderTypeMappings.put('G', "Forex - Swap");
        orderTypeMappings.put('H', "Forex - Previously Quoted");
        orderTypeMappings.put('I', "Funari");
        orderTypeMappings.put('P', "Pegged");
    }

    private boolean receivedOrder = false;
    private boolean receivedCancel = false;
    private boolean receivedReplace = false;
    private boolean rejectedCancelReplace = false;
    private char fixSide;
    private char fixOrderType;
    private char fixTimeInForce = '0';     // Day order if omitted
    private char fixStatus;
    private String id = null;
    private String clientOrderID = null;
    private String origClientID = null;
    private String symbol = null;
    private String securityID = null;
    private String idSource = null;
    private double quantity = 0.0;
    private double open = 0.0;
    private double executed = 0.0;
    private double priceLimit = 0.0;
    private double avgPx = 0.0;

    private static final Logger LOG = LoggerFactory.getLogger(Order.class);

    public Order () {
    }

    public Order(String id) {
        this.id = id;
    }

    @Override
    public Order clone() {
        try {
            Order order = (Order)super.clone();
            order.setOrigClientID(id());
            order.setID(generateID());
            return order;
        } catch ( CloneNotSupportedException e ) {
            LOG.error("Error: ", e);
            return null;
        }
    }

    public static String generateID() {
        return "O" + UUID.randomUUID();
    }

    @JsonProperty
    @Override
    public String id() {
        return id;
    }

    @JsonProperty
    public void setID(String id) {
        this.id = id;
    }

    @JsonProperty
    public String getClientOrderID() {
        return clientOrderID;
    }

    @JsonProperty
    public void setClientOrderID(String clientOrderID) {
        this.clientOrderID = clientOrderID;
    }

    @JsonProperty
    public String getOrigClientID() {
        return origClientID;
    }

    @JsonProperty
    public void setOrigClientID(String origClientID) {
        this.origClientID = origClientID;
    }

    @JsonProperty
    public double getPriceLimit() {
        return priceLimit;
    }

    @JsonProperty
    public void setPriceLimit(double priceLimit) {
        this.priceLimit = priceLimit;
    }

    @JsonProperty
    public double getAvgPx() {
        return avgPx;
    }

    @JsonProperty
    public void setAvgPx(double avgPx) {
        this.avgPx = avgPx;
    }

    @JsonProperty
    public double getExecuted() {
        return executed;
    }

    @JsonProperty
    public void setExecuted(double executed) {
        this.executed = executed;
    }

    @JsonProperty
    public double getOpen() {
        return open;
    }

    @JsonProperty
    public void setOpen(double open) {
        this.open = open;
    }

    @JsonProperty
    public double getQuantity() {
        return quantity;
    }

    @JsonProperty
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @JsonProperty
    public char getFixSide() {
        return fixSide;
    }

    @JsonProperty
    public void setFixSide(char fixSide) {
        this.fixSide = fixSide;
    }

    @JsonIgnore
    public String getStatus() {

        if (receivedOrder)
            return "Received";

        if (receivedCancel)
            return "Cancel Received";

        if (receivedReplace)
            return "Replace Received";

        if (rejectedCancelReplace)
            return "Cancel/Replace Rejected";

        return geStatusAsString();
    }

    private String geStatusAsString() {

        switch (fixStatus) {
            case '0':
                return "New";
            case '1':
                return "Partially filled";
            case '2':
                return "Filled";
            case '3':
                return "Done for day";
            case '4':
                return "Canceled";
            case '5':
                return "Replaced";
            case '6':
                return "Pending Cancel";
            case '7':
                return "Stopped";
            case '8':
                return "Rejected";
            case '9':
                return "Suspended";
            case 'A':
                return "Pending New";
            case 'B':
                return "Calculated";
            case 'C':
                return "Expired";
            case 'D':
                return "Accepted for bidding";
            case 'E':
                return "Pending Replace";
            default:
                return UNKNOWN;
        }
    }

    @JsonProperty
    public char getFixStatus() {
        return fixStatus;
    }

    @JsonProperty
    public void setFixStatus(char fixStatus) {
        this.fixStatus = fixStatus;
    }

    @JsonProperty
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonIgnore
    public String getTimeInForce() {
        switch (fixTimeInForce) {
            case '0':
                return "Day";
            case '1':
                return "GTC";
            case '2':
                return "OPG";
            case '3':
                return "IOC";
            case '4':
                return "FOK";
            case '5':
                return "GTX";
            case '6':
                return "GTD";
            default:
                return UNKNOWN;
        }
    }

    @JsonProperty
    public char getFixTimeInForce() {
        return fixTimeInForce;
    }

    @JsonProperty
    public void setFixTimeInForce(char fixTimeInForce) {
        this.fixTimeInForce = fixTimeInForce;
    }

    @JsonProperty
    public String getIdSource() {
        return idSource;
    }

    @JsonProperty
    public void setIdSource(String idSource) {
        this.idSource = idSource;
    }

    @JsonProperty
    public String getSecurityID() {
        return securityID;
    }

    @JsonProperty
    public void setSecurityID(String securityID) {
        this.securityID = securityID;
    }

    @JsonIgnore
    public String getOrderType() {

        return orderTypeMappings.containsKey(fixOrderType)
                ? orderTypeMappings.get(fixOrderType)
                : UNKNOWN;
    }

    @JsonProperty
    public char getFixOrderType() {
        return fixOrderType;
    }

    @JsonProperty
    public void setFixOrderType(char fixOrderType) {
        this.fixOrderType = fixOrderType;
    }

    @JsonProperty
    public boolean isReceivedCancel() {
        return receivedCancel;
    }

    @JsonProperty
    public void setReceivedCancel(boolean receivedCancel) {
        this.receivedCancel = receivedCancel;
    }

    @JsonProperty
    public boolean isReceivedOrder() {
        return receivedOrder;
    }

    @JsonProperty
    public void setReceivedOrder(boolean receivedOrder) {
        this.receivedOrder = receivedOrder;
    }

    @JsonProperty
    public boolean isReceivedReplace() {
        return receivedReplace;
    }

    @JsonProperty
    public void setReceivedReplace(boolean receivedReplace) {
        this.receivedReplace = receivedReplace;
    }

    @JsonProperty
    public boolean isRejectedCancelReplace() {
        return rejectedCancelReplace;
    }

    @JsonProperty
    public void setRejectedCancelReplace(boolean rejectedCancelReplace) {
        this.rejectedCancelReplace = rejectedCancelReplace;
    }

    @Override
    public String toString() {
        return "Order{" +
                "receivedOrder=" + receivedOrder +
                ", receivedCancel=" + receivedCancel +
                ", receivedReplace=" + receivedReplace +
                ", rejectedCancelReplace=" + rejectedCancelReplace +
                ", fixSide=" + fixSide +
                ", fixOrderType=" + fixOrderType +
                ", fixTimeInForce=" + fixTimeInForce +
                ", fixStatus=" + fixStatus +
                ", id='" + id + '\'' +
                ", clientOrderID='" + clientOrderID + '\'' +
                ", origClientID='" + origClientID + '\'' +
                ", symbol='" + symbol + '\'' +
                ", securityID='" + securityID + '\'' +
                ", idSource='" + idSource + '\'' +
                ", quantity=" + quantity +
                ", open=" + open +
                ", executed=" + executed +
                ", priceLimit=" + priceLimit +
                ", avgPx=" + avgPx +
                '}';
    }

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Order order = new Order("IBM");

        String value = objectMapper.writeValueAsString(order);

        System.out.println(value);

        Order order2 = objectMapper.readValue(value, Order.class);

        System.out.println(order2.toString());
    }
}
