package org.nprogramming.fiximulator2.domain;

import org.nprogramming.fiximulator2.api.ItemWithId;

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
    private String ID = null;
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

    @Override
    public Order clone() {
        try {
            Order order = (Order)super.clone();
            order.setOrigClientID(id());
            order.setID(generateID());
            return order;
        } catch ( CloneNotSupportedException e ) {
            return null;
        }
    }
    
    public Order () {
        ID = generateID();
    }

    public String generateID() {
        return "O" + UUID.randomUUID();
    }

    @Override
    public String id() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getSide() {
        if (side == '1') return "Buy";
        if (side == '2') return "Sell";
        if (side == '3') return "Buy minus";
        if (side == '4') return "Sell plus";
        if (side == '5') return "Sell short";
        if (side == '6') return "Sell short exempt";
        if (side == '7') return "Undisclosed";
        if (side == '8') return "Cross";
        if (side == '9') return "Cross short";
        return "<UNKNOWN>";
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
        if (status == '0') return "New";
        if (status == '1') return "Partially filled";
        if (status == '2') return "Filled";
        if (status == '3') return "Done for day";
        if (status == '4') return "Canceled";
        if (status == '5') return "Replaced";
        if (status == '6') return "Pending Cancel";
        if (status == '7') return "Stopped";
        if (status == '8') return "Rejected";
        if (status == '9') return "Suspended";
        if (status == 'A') return "Pending New";
        if (status == 'B') return "Calculated";
        if (status == 'C') return "Expired";
        if (status == 'D') return "Accepted for bidding";
        if (status == 'E') return "Pending Replace";
        return "<UNKNOWN>";
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
        if (timeInForce == '0') return "Day";
        if (timeInForce == '1') return "GTC";
        if (timeInForce == '2') return "OPG";
        if (timeInForce == '3') return "IOC";
        if (timeInForce == '4') return "FOK";
        if (timeInForce == '5') return "GTX";
        if (timeInForce == '6') return "GTD";
        return "<UNKNOWN>";
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
        if (orderType == '1') return "Market";
        if (orderType == '2') return "Limit";
        if (orderType == '3') return "Stop";
        if (orderType == '4') return "Stop limit";
        if (orderType == '5') return "Market on close";
        if (orderType == '6') return "With or without";
        if (orderType == '7') return "Limit or better";
        if (orderType == '8') return "Limit with or without";
        if (orderType == '9') return "On basis";
        if (orderType == 'A') return "On close";
        if (orderType == 'B') return "Limit on close";
        if (orderType == 'C') return "Forex - Market";
        if (orderType == 'D') return "Previously quoted";
        if (orderType == 'E') return "Previously indicated";
        if (orderType == 'F') return "Forex - Limit";
        if (orderType == 'G') return "Forex - Swap";
        if (orderType == 'H') return "Forex - Previously Quoted";
        if (orderType == 'I') return "Funari";
        if (orderType == 'P') return "Pegged";
        return "<UNKNOWN>";
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
