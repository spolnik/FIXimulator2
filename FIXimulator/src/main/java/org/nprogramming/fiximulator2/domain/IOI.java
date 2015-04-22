package org.nprogramming.fiximulator2.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class IOI implements Cloneable, ItemWithId {

    private String ID = null;
    private String refID = null;
    private String type = null;     // NEW, CANCEL, REPLACE
    private String side = "";       // BUY, SELL, UNDISCLOSED
    private Integer quantity = 0;
    private String symbol = "";
    private String securityID = "";
    private String iDSource = "";   //
    private double price = 0.0;
    private String natural = "";    // YES, NO

    private static final Logger LOG = LoggerFactory.getLogger(IOI.class);

    public IOI () {
        ID = generateID();
    }
    
    @Override
    public IOI clone() {
        try {
            IOI ioi = (IOI)super.clone();
            ioi.setRefID(id());
            ioi.setID(ioi.generateID());
            return ioi;
        } catch(CloneNotSupportedException e) {
            LOG.error("Error: ", e);
            return null;
        }
    }
    
    public String generateID() {
        return "I" + UUID.randomUUID();
    }

    @Override
    public String id() {
        return ID;
    }

    public void setID(String id) {
        this.ID = id;
    }

    public String getRefID() {
        return refID;
    }

    public void setRefID(String refID) {
        this.refID = refID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = "NEW";
        if (type.toUpperCase().startsWith("C"))
            this.type = "CANCEL";
        if (type.toUpperCase().startsWith("R"))
            this.type = "REPLACE";
    }
    
    public String getNatural() {
        return natural;
    }

    public void setNatural(String natural) {
        this.natural = "NO";
        if (natural.toUpperCase().startsWith("Y"))
            this.natural = "YES";
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSecurityID() {
        return securityID;
    }

    public void setSecurityID(String securityID) {
        this.securityID = securityID.equals("")
                ? "<MISSING>"
                : securityID;
    }
    
    public String getIDSource() {
        return iDSource;
    }

    public void setIDSource(String iDSource) {
        this.iDSource = "UNKNOWN";
        if (iDSource.toUpperCase().startsWith("C"))
            this.iDSource = "CUSIP";
        if (iDSource.toUpperCase().startsWith("S"))
            this.iDSource = "SEDOL";
        if (iDSource.toUpperCase().startsWith("T"))
            this.iDSource = "TICKER";
        if (iDSource.toUpperCase().startsWith("R"))
            this.iDSource = "RIC";
    }
    
    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = "BUY";
        if (side.toUpperCase().startsWith("S")) this.side = "SELL";
        if (side.toUpperCase().startsWith("U")) this.side = "UNDISCLOSED";
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol.equals("")
                ? "<MISSING>"
                : symbol;
    }
}
