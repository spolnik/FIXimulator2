package com.wordpress.nprogramming.oms.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class Execution implements Cloneable, ItemWithId {

    private static final Logger LOG = LoggerFactory.getLogger(Execution.class);

    private boolean dkd = false;
    private String id = null;
    private String refID = null;
    private char execType;
    private char execTranType;
    private double lastShares = 0.0;
    private double lastPx = 0.0;
    private double leavesQty = 0.0;
    private double cumQty = 0.0;
    private double avgPx = 0.0;
    private String clientOrderID;
    private double orderQuantity;
    private char fixStatus;
    private String symbol;
    private char fixSide;
    private String securityID;
    private String idSource;
    private String orderId;
    private double executed;
    private double open;

    private Execution(Order order) {
        id = generateID();
    }

    public static Execution createFrom(Order order) {
        Execution execution = new Execution(order);

        execution.setClientOrderID(
                order.getClientOrderID()
        );
        execution.setOrderQuantity(
                order.getQuantity()
        );
        execution.setFixStatus(
                order.getFIXStatus()
        );
        execution.setSymbol(
                order.getSymbol()
        );
        execution.setFixSide(
                order.getFIXSide()
        );
        execution.setSecurityID(
                order.getSecurityID()
        );
        execution.setIdSource(
                order.getIdSource()
        );
        execution.setOrderId(
                order.id()
        );
        execution.setExecuted(
                order.getExecuted()
        );

        return execution;
    }

    @Override
    public Execution clone() {
        try {
            Execution execution = (Execution) super.clone();
            execution.setRefID(id());
            execution.setId(generateID());
            execution.setDKd(false);
            return execution;
        } catch (CloneNotSupportedException e) {
            LOG.error("Error: ", e);
            return null;
        }
    }

    public String generateID() {
        return "E" + UUID.randomUUID();
    }

    @Override
    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDKd() {
        return dkd;
    }

    public void setDKd(boolean dkd) {
        this.dkd = dkd;
    }

    public double getAvgPx() {
        return avgPx;
    }

    public void setAvgPx(double avgPx) {
        this.avgPx = avgPx;
    }

    public double getCumQty() {
        return cumQty;
    }

    public void setCumQty(double cumQty) {
        this.cumQty = cumQty;
    }

    public String getExecTranType() {
        switch (execTranType) {
            case '0':
                return "New";
            case '1':
                return "Cancel";
            case '2':
                return "Correct";
            case '3':
                return "Status";
            default:
                return "<UNKOWN>";
        }
    }

    public void setExecTranType(char execTranType) {
        this.execTranType = execTranType;
    }

    public char getFIXExecTranType() {
        return execTranType;
    }

    public String getExecType() {
        switch (execType) {
            case '0':
                return "New";
            case '1':
                return "Partial fill";
            case '2':
                return "Fill";
            case '3':
                return "Done for day";
            case '4':
                return "Canceled";
            case '5':
                return "Replace";
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
                return "Restated";
            case 'E':
                return "Pending Replace";
            default:
                return "<UNKNOWN>";
        }
    }

    public void setExecType(char execType) {
        this.execType = execType;
    }

    public char getFIXExecType() {
        return execType;
    }

    public double getLastPx() {
        return lastPx;
    }

    public void setLastPx(double lastPx) {
        this.lastPx = lastPx;
    }

    public double getLastShares() {
        return lastShares;
    }

    public void setLastShares(double lastShares) {
        this.lastShares = lastShares;
    }

    public double getLeavesQty() {
        return leavesQty;
    }

    public void setLeavesQty(double leavesQty) {
        this.leavesQty = leavesQty;
    }

    public String getRefID() {
        return refID;
    }

    public void setRefID(String refID) {
        this.refID = refID;
    }

    public String getClientOrderID() {
        return clientOrderID;
    }

    public void setClientOrderID(String clientOrderID) {
        this.clientOrderID = clientOrderID;
    }

    public void setDkd(boolean dkd) {
        this.dkd = dkd;
    }

    public void setFixStatus(char fixStatus) {
        this.fixStatus = fixStatus;
    }

    public void setFixSide(char fixSide) {
        this.fixSide = fixSide;
    }

    public double getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(double orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public char getFIXStatus() {
        return fixStatus;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public char getFIXSide() {
        return fixSide;
    }

    public String getSecurityID() {
        return securityID;
    }

    public void setSecurityID(String securityID) {
        this.securityID = securityID;
    }

    public String getIdSource() {
        return idSource;
    }

    public void setIdSource(String idSource) {
        this.idSource = idSource;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getExecuted() {
        return executed;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public void setStatus(char status) {
        this.fixStatus = status;
    }

    public void setExecuted(double executed) {
        this.executed = executed;
    }

    public double getOpen() {
        return open;
    }

}
