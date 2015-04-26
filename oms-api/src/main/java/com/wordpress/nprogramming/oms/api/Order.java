package com.wordpress.nprogramming.oms.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private char side;
    private char orderType;
    private char timeInForce = '0';     // Day order if omitted
    private char status;
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
        id = generateID();
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

    public String generateID() {
        return "O" + UUID.randomUUID();
    }

    @Override
    public String id() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getClientOrderID() {
        return clientOrderID;
    }

    public void setClientOrderID(String clientOrderID) {
        this.clientOrderID = clientOrderID;
    }

    public String getOrigClientID() {
        return origClientID;
    }

    public void setOrigClientID(String origClientID) {
        this.origClientID = origClientID;
    }

    public double getPriceLimit() {
        return priceLimit;
    }

    public void setPriceLimit(double priceLimit) {
        this.priceLimit = priceLimit;
    }

    public double getAvgPx() {
        return avgPx;
    }

    public void setAvgPx(double avgPx) {
        this.avgPx = avgPx;
    }

    public double getExecuted() {
        return executed;
    }

    public void setExecuted(double executed) {
        this.executed = executed;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    
    public char getFIXSide() {
        return side;
    }

    public void setSide(char side) {
        this.side = side;
    }

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

        switch (status) {
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

    public char getFIXStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTimeInForce() {
        switch (timeInForce) {
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
    
    public char getFIXTimeInForce() {
        return timeInForce;
    }
    
    public void setTimeInForce(char timeInForce) {
        this.timeInForce = timeInForce;
    }

    public String getIdSource() {
        return idSource;
    }

    public void setIdSource(String idSource) {
        this.idSource = idSource;
    }

    public String getSecurityID() {
        return securityID;
    }

    public void setSecurityID(String securityID) {
        this.securityID = securityID;
    }

    public String getOrderType() {

        return orderTypeMappings.containsKey(orderType)
                ? orderTypeMappings.get(orderType)
                : UNKNOWN;
    }
    
    public char getFIXOrderType() {
        return orderType;
    }

    public void setOrderType(char orderType) {
        this.orderType = orderType;
    }

    public boolean isReceivedCancel() {
        return receivedCancel;
    }

    public void setReceivedCancel(boolean receivedCancel) {
        this.receivedCancel = receivedCancel;
    }

    public boolean isReceivedOrder() {
        return receivedOrder;
    }

    public void setReceivedOrder(boolean receivedOrder) {
        this.receivedOrder = receivedOrder;
    }

    public boolean isReceivedReplace() {
        return receivedReplace;
    }

    public void setReceivedReplace(boolean receivedReplace) {
        this.receivedReplace = receivedReplace;
    }

    public boolean isRejectedCancelReplace() {
        return rejectedCancelReplace;
    }

    public void setRejectedCancelReplace(boolean rejectedCancelReplace) {
        this.rejectedCancelReplace = rejectedCancelReplace;
    }
}
