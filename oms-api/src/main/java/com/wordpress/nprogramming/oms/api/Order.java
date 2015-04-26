package com.wordpress.nprogramming.oms.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class Order implements Cloneable, ItemWithId {

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
        if (receivedOrder) return "Received";
        if (receivedCancel) return "Cancel Received";
        if (receivedReplace) return "Replace Received";
        if (rejectedCancelReplace) return "Cancel/Replace Rejected";

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
                return "<UNKNOWN>";
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
                return "<UNKNOWN>";
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
        switch (orderType) {
            case '1':
                return "Market";
            case '2':
                return "Limit";
            case '3':
                return "Stop";
            case '4':
                return "Stop limit";
            case '5':
                return "Market on close";
            case '6':
                return "With or without";
            case '7':
                return "Limit or better";
            case '8':
                return "Limit with or without";
            case '9':
                return "On basis";
            case 'A':
                return "On close";
            case 'B':
                return "Limit on close";
            case 'C':
                return "Forex - Market";
            case 'D':
                return "Previously quoted";
            case 'E':
                return "Previously indicated";
            case 'F':
                return "Forex - Limit";
            case 'G':
                return "Forex - Swap";
            case 'H':
                return "Forex - Previously Quoted";
            case 'I':
                return "Funari";
            case 'P':
                return "Pegged";
            default:
                return "<UNKNOWN>";
        }
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
