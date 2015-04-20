package org.nprogramming.fiximulator2.fix;

import com.wordpress.nprogramming.instruments.api.Instrument;
import com.wordpress.nprogramming.instruments.api.InstrumentsApi;
import org.nprogramming.fiximulator2.api.NotifyService;
import org.nprogramming.fiximulator2.api.Repository;
import org.nprogramming.fiximulator2.domain.IOI;
import quickfix.field.*;

import java.util.Date;

final class FixIOISender {

    private FixMessageSender fixMessageSender;
    private InstrumentsApi instrumentsApi;
    private Repository<IOI> ioiRepository;
    private final NotifyService notifyService;

    public FixIOISender(
            FixMessageSender fixMessageSender,
            InstrumentsApi instrumentsApi,
            Repository<IOI> ioiRepository,
            NotifyService notifyService
    ) {

        this.fixMessageSender = fixMessageSender;
        this.instrumentsApi = instrumentsApi;
        this.ioiRepository = ioiRepository;
        this.notifyService = notifyService;
    }

    public void send(IOI ioi) {
        // *** Required fields ***
        // IOIid
        IOIid ioiID = new IOIid(ioi.id());

        // IOITransType
        IOITransType ioiType = null;
        if ("NEW".equals(ioi.getType()))
            ioiType = new IOITransType(IOITransType.NEW);
        if ("CANCEL".equals(ioi.getType()))
            ioiType = new IOITransType(IOITransType.CANCEL);
        if ("REPLACE".equals(ioi.getType()))
            ioiType = new IOITransType(IOITransType.REPLACE);

        // Side
        Side side = null;
        if ("BUY".equals(ioi.getSide()))
            side = new Side(Side.BUY);
        if ("SELL".equals(ioi.getSide()))
            side = new Side(Side.SELL);
        if ("UNDISCLOSED".equals(ioi.getSide()))
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
        if ("CANCEL".equals(ioi.getType()) || "REPLACE".equals(ioi.getType())) {
            ioiRefID = new IOIRefID(ioi.getRefID());
            fixIOI.set(ioiRefID);
        }

        // *** Optional fields ***
        // SecurityID
        SecurityID securityID = new SecurityID(ioi.getSecurityID());
        fixIOI.set(securityID);

        // IDSource
        IDSource idSource = null;
        if ("TICKER".equals(ioi.getIDSource()))
            idSource = new IDSource(IDSource.EXCHANGE_SYMBOL);
        if ("RIC".equals(ioi.getIDSource()))
            idSource = new IDSource(IDSource.RIC_CODE);
        if ("SEDOL".equals(ioi.getIDSource()))
            idSource = new IDSource(IDSource.SEDOL);
        if ("CUSIP".equals(ioi.getIDSource()))
            idSource = new IDSource(IDSource.CUSIP);
        if ("UNKOWN".equals(ioi.getIDSource()))
            idSource = new IDSource("100");
        fixIOI.set(idSource);

        // Price
        Price price = new Price(ioi.getPrice());
        fixIOI.set(price);

        // IOINaturalFlag
        IOINaturalFlag ioiNaturalFlag = new IOINaturalFlag();
        if ("YES".equals(ioi.getNatural()))
            ioiNaturalFlag.setValue(true);
        if ("NO".equals(ioi.getNatural()))
            ioiNaturalFlag.setValue(false);
        fixIOI.set(ioiNaturalFlag);

        // SecurityDesc
        Instrument instrument =
                instrumentsApi.getInstrument(ioi.getSymbol());
        String name = "Unknown security";
        if (instrument != null)
            name = instrument.getName();
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
        notifyService.sendChangedIOIId(ioi.id());
    }
}
