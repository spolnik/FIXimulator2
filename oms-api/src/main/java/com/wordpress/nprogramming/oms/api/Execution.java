package com.wordpress.nprogramming.oms.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class Execution implements Cloneable, ItemWithId {

    private Order order;
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

    private static final Logger LOG = LoggerFactory.getLogger(Execution.class);

    public Execution( Order order ) {
        id = generateID();
        this.order = order;
    }

    @Override
    public Execution clone() {
        try {
            Execution execution = (Execution)super.clone();
            execution.setRefID(id());
            execution.setId(generateID());
            execution.setDKd(false);
            return execution;
        } catch(CloneNotSupportedException e) {
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

    public char getFIXExecTranType() {
        return execTranType;
    }
    
    public void setExecTranType(char execTranType) {
        this.execTranType = execTranType;
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
    
    public char getFIXExecType() {
        return execType;
    }

    public void setExecType(char execType) {
        this.execType = execType;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getRefID() {
        return refID;
    }

    public void setRefID(String refID) {
        this.refID = refID;
    }
}
