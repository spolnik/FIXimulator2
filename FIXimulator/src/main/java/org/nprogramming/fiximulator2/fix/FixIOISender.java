package org.nprogramming.fiximulator2.fix;

import org.nprogramming.fiximulator2.api.InstrumentsApi;
import org.nprogramming.fiximulator2.api.RepositoryWithCallback;
import org.nprogramming.fiximulator2.domain.IOI;
import org.nprogramming.fiximulator2.domain.Instrument;
import quickfix.field.*;

import java.util.Date;

final class FixIOISender {

    private FixMessageSender fixMessageSender;
    private InstrumentsApi instrumentRepository;
    private RepositoryWithCallback<IOI> ioiRepository;

    public FixIOISender(
            FixMessageSender fixMessageSender,
            InstrumentsApi instrumentRepository,
            RepositoryWithCallback<IOI> ioiRepository) {

        this.fixMessageSender = fixMessageSender;
        this.instrumentRepository = instrumentRepository;
        this.ioiRepository = ioiRepository;
    }

    public void send(IOI ioi) {
        // *** Required fields ***
        // IOIid
        IOIid ioiID = new IOIid(ioi.id());

        // IOITransType
        IOITransType ioiType = null;
        if (ioi.getType().equals("NEW"))
            ioiType = new IOITransType(IOITransType.NEW);
        if (ioi.getType().equals("CANCEL"))
            ioiType = new IOITransType(IOITransType.CANCEL);
        if (ioi.getType().equals("REPLACE"))
            ioiType = new IOITransType(IOITransType.REPLACE);

        // Side
        Side side = null;
        if (ioi.getSide().equals("BUY")) side = new Side(Side.BUY);
        if (ioi.getSide().equals("SELL")) side = new Side(Side.SELL);
        if (ioi.getSide().equals("UNDISCLOSED"))
            side = new Side(Side.UNDISCLOSED);

        // IOIShares
        IOIShares shares = new IOIShares(ioi.getQuantity().toString());

        // Symbol
        Symbol symbol = new Symbol(ioi.getSymbol());

        // Construct IOI from required fields
        quickfix.fix42.IndicationofInterest fixIOI =
                new quickfix.fix42.IndicationofInterest(
                        ioiID, ioiType, symbol, side, shares);

        // *** Conditionally required fields ***
        // IOIRefID
        IOIRefID ioiRefID;
        if (ioi.getType().equals("CANCEL") || ioi.getType().equals("REPLACE")) {
            ioiRefID = new IOIRefID(ioi.getRefID());
            fixIOI.set(ioiRefID);
        }

        // *** Optional fields ***
        // SecurityID
        SecurityID securityID = new SecurityID(ioi.getSecurityID());
        fixIOI.set(securityID);

        // IDSource
        IDSource idSource = null;
        if (ioi.getIDSource().equals("TICKER"))
            idSource = new IDSource(IDSource.EXCHANGE_SYMBOL);
        if (ioi.getIDSource().equals("RIC"))
            idSource = new IDSource(IDSource.RIC_CODE);
        if (ioi.getIDSource().equals("SEDOL"))
            idSource = new IDSource(IDSource.SEDOL);
        if (ioi.getIDSource().equals("CUSIP"))
            idSource = new IDSource(IDSource.CUSIP);
        if (ioi.getIDSource().equals("UNKOWN"))
            idSource = new IDSource("100");
        fixIOI.set(idSource);

        // Price
        Price price = new Price(ioi.getPrice());
        fixIOI.set(price);

        // IOINaturalFlag
        IOINaturalFlag ioiNaturalFlag = new IOINaturalFlag();
        if (ioi.getNatural().equals("YES"))
            ioiNaturalFlag.setValue(true);
        if (ioi.getNatural().equals("NO"))
            ioiNaturalFlag.setValue(false);
        fixIOI.set(ioiNaturalFlag);

        // SecurityDesc
        Instrument instrument =
                instrumentRepository.getInstrument(ioi.getSymbol());
        String name = "Unknown security";
        if (instrument != null) name = instrument.getName();
        SecurityDesc desc = new SecurityDesc(name);
        fixIOI.set(desc);

        // ValidUntilTime
        int minutes = 30;
        long expiry = new Date().getTime() + 1000 * 60 * minutes;
        Date validUntil = new Date(expiry);
        ValidUntilTime validTime = new ValidUntilTime(validUntil);
        fixIOI.set(validTime);

        //Currency
        Currency currency = new Currency("USD");
        fixIOI.set(currency);

        // *** Send message ***
        fixMessageSender.send(fixIOI);
        ioiRepository.save(ioi);
    }
}
