package com.wordpress.nprogramming.oms.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

import static com.google.common.base.Strings.isNullOrEmpty;

public class IOI implements Cloneable, ItemWithId {

    private static final Logger LOG = LoggerFactory.getLogger(IOI.class);

    private String id = null;
    private String refID = null;
    private String type = null;     // NEW, CANCEL, REPLACE
    private String side = "";       // BUY, SELL, UNDISCLOSED
    private Integer quantity = 0;
    private String symbol = "";
    private String securityID = "";
    private String idSource = "";
    private double price = 0.0;
    private String natural = "";    // YES, NO

    public IOI() {
    }

    public IOI(String id) {
        this.id = id;
    }

    @Override
    public IOI clone() {
        try {
            IOI ioi = (IOI) super.clone();
            ioi.setRefID(id());
            ioi.setId(generateID());
            return ioi;
        } catch (CloneNotSupportedException e) {
            LOG.error("Error: ", e);
            return null;
        }
    }

    public static String generateID() {
        return "I" + UUID.randomUUID();
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
    public String getRefID() {
        return refID;
    }

    @JsonProperty
    public void setRefID(String refID) {
        this.refID = refID;
    }

    @JsonProperty
    public String getType() {
        return type;
    }

    @JsonProperty
    public void setType(String type) {
        this.type = "NEW";
        if (type != null && type.toUpperCase().startsWith("C"))
            this.type = "CANCEL";
        if (type != null && type.toUpperCase().startsWith("R"))
            this.type = "REPLACE";
    }

    @JsonProperty
    public String getNatural() {
        return natural;
    }

    @JsonProperty
    public void setNatural(String natural) {
        this.natural = "NO";
        if (natural.toUpperCase().startsWith("Y"))
            this.natural = "YES";
    }

    @JsonProperty
    public double getPrice() {
        return price;
    }

    @JsonProperty
    public void setPrice(double price) {
        this.price = price;
    }

    @JsonProperty
    public Integer getQuantity() {
        return quantity;
    }

    @JsonProperty
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @JsonProperty
    public String getSecurityID() {
        return securityID;
    }

    @JsonProperty
    public void setSecurityID(String securityID) {
        this.securityID = isNullOrEmpty(securityID)
                ? "<MISSING>"
                : securityID;
    }

    @JsonProperty
    public String getIDSource() {
        return idSource;
    }

    @JsonProperty
    public void setIDSource(String iDSource) {
        this.idSource = "UNKNOWN";
        if (iDSource.toUpperCase().startsWith("C"))
            this.idSource = "CUSIP";
        if (iDSource.toUpperCase().startsWith("S"))
            this.idSource = "SEDOL";
        if (iDSource.toUpperCase().startsWith("T"))
            this.idSource = "TICKER";
        if (iDSource.toUpperCase().startsWith("R"))
            this.idSource = "RIC";
    }

    @JsonProperty
    public String getSide() {
        return side;
    }

    @JsonProperty
    public void setSide(String side) {
        this.side = "BUY";

        if (side.toUpperCase().startsWith("S"))
            this.side = "SELL";

        if (side.toUpperCase().startsWith("U"))
            this.side = "UNDISCLOSED";
    }

    @JsonProperty
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty
    public void setSymbol(String symbol) {
        this.symbol = isNullOrEmpty(symbol)
                ? "<MISSING>"
                : symbol;
    }

    @Override
    public String toString() {
        return "IOI{" +
                "id='" + id + '\'' +
                ", refID='" + refID + '\'' +
                ", type='" + type + '\'' +
                ", side='" + side + '\'' +
                ", quantity=" + quantity +
                ", symbol='" + symbol + '\'' +
                ", securityID='" + securityID + '\'' +
                ", idSource='" + idSource + '\'' +
                ", price=" + price +
                ", natural='" + natural + '\'' +
                '}';
    }

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        IOI ioi = new IOI("IBM");

        String value = objectMapper.writeValueAsString(ioi);

        System.out.println(value);

        IOI ioi2 = objectMapper.readValue(value, IOI.class);

        System.out.println(ioi2.toString());
    }
}
