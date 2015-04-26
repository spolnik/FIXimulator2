package com.wordpress.nprogramming.oms.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public class Execution implements Cloneable, ItemWithId {

    private static final Logger LOG = LoggerFactory.getLogger(Execution.class);

    private boolean dkd = false;
    private String id = null;
    private String refID = null;
    private char fixExecType;
    private char fixExecTranType;
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

    private Execution() {
    }

    public Execution(String id) {
        this.id = id;
    }

    public static Execution createFrom(Order order) {
        Execution execution = new Execution(generateID());

        execution.setClientOrderID(
                order.getClientOrderID()
        );
        execution.setOrderQuantity(
                order.getQuantity()
        );
        execution.setFixStatus(
                order.getFixStatus()
        );
        execution.setSymbol(
                order.getSymbol()
        );
        execution.setFixSide(
                order.getFixSide()
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

    public static String generateID() {
        return "E" + UUID.randomUUID();
    }

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Execution execution = new Execution("IBM");

        String value = objectMapper.writeValueAsString(execution);

        System.out.println(value);

        Execution execution2 = objectMapper.readValue(value, Execution.class);

        System.out.println(execution2.toString());
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

    @JsonProperty
    @Override
    public String id() {
        return id;
    }

    @JsonProperty
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty
    public boolean isDKd() {
        return dkd;
    }

    @JsonProperty
    public void setDKd(boolean dkd) {
        this.dkd = dkd;
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
    public double getCumQty() {
        return cumQty;
    }

    @JsonProperty
    public void setCumQty(double cumQty) {
        this.cumQty = cumQty;
    }

    @JsonIgnore
    public String getExecTranType() {
        switch (fixExecTranType) {
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

    @JsonProperty
    public void setFixExecTranType(char execTranType) {
        this.fixExecTranType = execTranType;
    }

    @JsonProperty
    public char getFixExecTranType() {
        return fixExecTranType;
    }

    @JsonProperty
    public void setFixExecType(char fixExecType) {
        this.fixExecType = fixExecType;
    }

    @JsonProperty
    public char getFixExecType() {
        return fixExecType;
    }

    @JsonProperty
    public double getLastPx() {
        return lastPx;
    }

    @JsonProperty
    public void setLastPx(double lastPx) {
        this.lastPx = lastPx;
    }

    @JsonProperty
    public double getLastShares() {
        return lastShares;
    }

    @JsonProperty
    public void setLastShares(double lastShares) {
        this.lastShares = lastShares;
    }

    @JsonProperty
    public double getLeavesQty() {
        return leavesQty;
    }

    @JsonProperty
    public void setLeavesQty(double leavesQty) {
        this.leavesQty = leavesQty;
    }

    @JsonProperty
    public String getRefID() {
        return refID;
    }

    @JsonProperty
    public void setRefID(String refID) {
        this.refID = refID;
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
    public double getOrderQuantity() {
        return orderQuantity;
    }

    @JsonProperty
    public void setOrderQuantity(double orderQuantity) {
        this.orderQuantity = orderQuantity;
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

    @JsonProperty
    public char getFixSide() {
        return fixSide;
    }

    @JsonProperty
    public void setFixSide(char fixSide) {
        this.fixSide = fixSide;
    }

    @JsonProperty
    public String getSecurityID() {
        return securityID;
    }

    @JsonProperty
    public void setSecurityID(String securityID) {
        this.securityID = securityID;
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
    public String getOrderId() {
        return orderId;
    }

    @JsonProperty
    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    @Override
    public String toString() {
        return "Execution{" +
                "dkd=" + dkd +
                ", id='" + id + '\'' +
                ", refID='" + refID + '\'' +
                ", fixExecType=" + fixExecType +
                ", fixExecTranType=" + fixExecTranType +
                ", lastShares=" + lastShares +
                ", lastPx=" + lastPx +
                ", leavesQty=" + leavesQty +
                ", cumQty=" + cumQty +
                ", avgPx=" + avgPx +
                ", clientOrderID='" + clientOrderID + '\'' +
                ", orderQuantity=" + orderQuantity +
                ", fixStatus=" + fixStatus +
                ", symbol='" + symbol + '\'' +
                ", fixSide=" + fixSide +
                ", securityID='" + securityID + '\'' +
                ", idSource='" + idSource + '\'' +
                ", orderId='" + orderId + '\'' +
                ", executed=" + executed +
                ", open=" + open +
                '}';
    }
}
