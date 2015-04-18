package org.nprogramming.fiximulator2.fix;

import org.nprogramming.fiximulator2.api.*;
import org.nprogramming.fiximulator2.core.LogMessageSet;
import org.nprogramming.fiximulator2.core.StatusSwitcher;
import org.nprogramming.fiximulator2.domain.Execution;
import org.nprogramming.fiximulator2.domain.IOI;
import org.nprogramming.fiximulator2.domain.Instrument;
import org.nprogramming.fiximulator2.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix42.Message.Header;
import quickfix.fix42.OrderCancelReject;

import java.io.*;
import java.util.Date;
import java.util.Random;

public class FIXimulatorApplication extends MessageCracker
        implements Application {

    private static final Logger LOG = LoggerFactory.getLogger(FIXimulatorApplication.class);

    private boolean connected;
    private StatusSwitcher connectedStatus;
    private StatusSwitcher ioiSenderStatus;
    private StatusSwitcher executorStatus;
    private boolean ioiSenderStarted;
    private boolean executorStarted;
    private IOIsender ioiSender;
    private Thread ioiSenderThread;
    private Executor executor;
    private Thread executorThread;
    private LogMessageSet messages;
    private final OrdersApi ordersApi;
    private final RepositoryWithCallback<Execution> executionRepository;
    private final RepositoryWithCallback<IOI> ioiRepository;
    private final InstrumentsApi instrumentsApi;
    private final OrderFixTranslator orderFixTranslator;
    private SessionSettings settings;
    private SessionID currentSession;
    private DataDictionary dictionary;
    private Random random = new Random();

    public FIXimulatorApplication(
            SessionSettings settings,
            LogMessageSet messages,
            OrdersApi ordersApi,
            RepositoryWithCallback<Execution> executionRepository,
            RepositoryWithCallback<IOI> ioiRepository,
            InstrumentsApi instrumentsApi,
            OrderFixTranslator orderFixTranslator
    ) {
        this.settings = settings;
        this.messages = messages;
        this.ordersApi = ordersApi;
        this.executionRepository = executionRepository;
        this.ioiRepository = ioiRepository;
        this.instrumentsApi = instrumentsApi;
        this.orderFixTranslator = orderFixTranslator;
    }

    public void onCreate(SessionID sessionID) {
    }

    @Override
    public void onLogon(SessionID sessionID) {
        connected = true;
        currentSession = sessionID;
        dictionary = Session.lookupSession(currentSession).getDataDictionary();
        if (connectedStatus != null)
            connectedStatus.on();
    }

    @Override
    public void onLogout(SessionID sessionID) {
        connected = false;
        currentSession = null;
        connectedStatus.off();
    }

    // IndicationofInterest handling
    @Override
    public void onMessage(quickfix.fix42.IndicationofInterest message,
                          SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
    }

    // NewOrderSingle handling
    @Override
    public void onMessage(quickfix.fix42.NewOrderSingle message,
                          SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        Order order = orderFixTranslator.from(message);
        order.setReceivedOrder(true);
        if (executorStarted) {
            ordersApi.addOrderToFill(order);
            executorThread.interrupt();
        } else {
            ordersApi.addOrder(order);
            boolean autoAck = false;
            try {
                autoAck = settings.getBool("FIXimulatorAutoAcknowledge");
            } catch (Exception e) {
                LOG.error("Error: ", e);
            }
            if (autoAck) {
                acknowledge(order);
            }
        }
    }

    // OrderCancelRequest handling
    @Override
    public void onMessage(quickfix.fix42.OrderCancelRequest message,
                          SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        Order order = orderFixTranslator.from(message);
        order.setReceivedCancel(true);
        ordersApi.addOrder(order);
        boolean autoPending = false;
        boolean autoCancel = false;
        try {
            autoPending = settings.getBool("FIXimulatorAutoPendingCancel");
            autoCancel = settings.getBool("FIXimulatorAutoCancel");
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
        if (autoPending) {
            pendingCancel(order);
        }
        if (autoCancel) {
            cancel(order);
        }
    }

    // OrderReplaceRequest handling
    @Override
    public void onMessage(quickfix.fix42.OrderCancelReplaceRequest message,
                          SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        Order order = orderFixTranslator.from(message);
        order.setReceivedReplace(true);
        ordersApi.addOrder(order);
        boolean autoPending = false;
        boolean autoCancel = false;
        try {
            autoPending = settings.getBool("FIXimulatorAutoPendingReplace");
            autoCancel = settings.getBool("FIXimulatorAutoReplace");
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
        if (autoPending) {
            pendingReplace(order);
        }
        if (autoCancel) {
            replace(order);
        }
    }

    // OrderCancelReject handling
    @Override
    public void onMessage(quickfix.fix42.OrderCancelReject message,
                          SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
    }

    // ExecutionReport handling
    @Override
    public void onMessage(quickfix.fix42.ExecutionReport message,
                          SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
    }

    @Override
    public void onMessage(quickfix.fix42.DontKnowTrade message,
                          SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {

        try {
            ExecID execID = new ExecID();
            message.get(execID);
            Execution execution =
                    executionRepository.get(execID.getValue());
            execution.setDKd(true);
            executionRepository.update(execution.id());
        } catch (FieldNotFound ex) {
            LOG.error("Error: ", ex);
        }
    }

    public void fromApp(Message message, SessionID sessionID)
            throws FieldNotFound, IncorrectDataFormat,
            IncorrectTagValue, UnsupportedMessageType {
        messages.add(message, true, dictionary, sessionID);
        crack(message, sessionID);
    }

    public void toApp(Message message, SessionID sessionID) throws DoNotSend {
        try {
            messages.add(message, false, dictionary, sessionID);
            crack(message, sessionID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fromAdmin(Message message, SessionID sessionID)
            throws
            FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
    }

    public void toAdmin(Message message, SessionID sessionID) {
    }

    public void addStatusCallbacks(StatusSwitcher connectedStatus,
                                   StatusSwitcher ioiSenderStatus, StatusSwitcher executorStatus) {
        this.connectedStatus = connectedStatus;
        this.ioiSenderStatus = ioiSenderStatus;
        this.executorStatus = executorStatus;
    }

    public boolean getConnectionStatus() {
        return connected;
    }

    public SessionSettings getSettings() {
        return settings;
    }

    public void saveSettings() {
        try {
            OutputStream outputStream =
                    new BufferedOutputStream(
                            new FileOutputStream(
                                    new File("config/FIXimulator.cfg")));
            settings.toStream(outputStream);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    // Message handling methods
    public void acknowledge(Order order) {
        Execution acknowledgement = new Execution(order);
        order.setStatus(OrdStatus.NEW);
        acknowledgement.setExecType(ExecType.NEW);
        acknowledgement.setExecTranType(ExecTransType.NEW);
        acknowledgement.setLeavesQty(order.getOpen());
        sendExecution(acknowledgement);
        order.setReceivedOrder(false);
        ordersApi.update();
    }

    public void reject(Order order) {
        Execution reject = new Execution(order);
        order.setStatus(OrdStatus.REJECTED);
        reject.setExecType(ExecType.REJECTED);
        reject.setExecTranType(ExecTransType.NEW);
        reject.setLeavesQty(order.getOpen());
        sendExecution(reject);
        order.setReceivedOrder(false);
        ordersApi.update();
    }

    public void dfd(Order order) {
        Execution dfd = new Execution(order);
        order.setStatus(OrdStatus.DONE_FOR_DAY);
        dfd.setExecType(ExecType.DONE_FOR_DAY);
        dfd.setExecTranType(ExecTransType.NEW);
        dfd.setLeavesQty(order.getOpen());
        dfd.setCumQty(order.getExecuted());
        dfd.setAvgPx(order.getAvgPx());
        sendExecution(dfd);
        ordersApi.update();
    }

    public void pendingCancel(Order order) {
        Execution pending = new Execution(order);
        order.setStatus(OrdStatus.PENDING_CANCEL);
        pending.setExecType(ExecType.PENDING_CANCEL);
        pending.setExecTranType(ExecTransType.NEW);
        pending.setLeavesQty(order.getOpen());
        pending.setCumQty(order.getExecuted());
        pending.setAvgPx(order.getAvgPx());
        sendExecution(pending);
        order.setReceivedCancel(false);
        ordersApi.update();
    }

    public void cancel(Order order) {
        Execution cancel = new Execution(order);
        order.setStatus(OrdStatus.CANCELED);
        cancel.setExecType(ExecType.CANCELED);
        cancel.setExecTranType(ExecTransType.NEW);
        cancel.setLeavesQty(order.getOpen());
        cancel.setCumQty(order.getExecuted());
        cancel.setAvgPx(order.getAvgPx());
        sendExecution(cancel);
        order.setReceivedCancel(false);
        ordersApi.update();
    }

    public void rejectCancelReplace(Order order, boolean cancel) {
        quickfix.fix42.OrderCancelReject rejectMessage = rejectCancelReplaceOrder(order, cancel);

        // *** Send message ***
        sendMessage(rejectMessage);
        ordersApi.update();
    }

    private OrderCancelReject rejectCancelReplaceOrder(Order order, boolean cancel) {
        order.setReceivedCancel(false);
        order.setReceivedReplace(false);
        // *** Required fields ***
        // OrderID (37)
        OrderID orderID = new OrderID(order.getID());

        ClOrdID clientID = new ClOrdID(order.getClientOrderID());

        OrigClOrdID origClientID = new OrigClOrdID(order.getOrigClientID());

        // OrdStatus (39) Status as a result of this report
        if (order.getStatus().equals("<UNKNOWN>"))
            order.setStatus(OrdStatus.NEW);
        OrdStatus ordStatus = new OrdStatus(order.getFIXStatus());

        CxlRejResponseTo responseTo = new CxlRejResponseTo();
        if (cancel) {
            responseTo.setValue(CxlRejResponseTo.ORDER_CANCEL_REQUEST);
        } else {
            responseTo.setValue(CxlRejResponseTo.ORDER_CANCEL_REPLACE_REQUEST);
        }

        // Construct OrderCancelReject message from required fields
        return new OrderCancelReject(
                orderID,
                clientID,
                origClientID,
                ordStatus,
                responseTo);
    }

    public void pendingReplace(Order order) {
        Execution pending = new Execution(order);
        order.setStatus(OrdStatus.PENDING_REPLACE);
        pending.setExecType(ExecType.PENDING_REPLACE);
        pending.setExecTranType(ExecTransType.NEW);
        pending.setLeavesQty(order.getOpen());
        pending.setCumQty(order.getExecuted());
        pending.setAvgPx(order.getAvgPx());
        order.setReceivedReplace(false);
        sendExecution(pending);
        ordersApi.update();
    }

    public void replace(Order order) {
        Execution replace = new Execution(order);
        order.setStatus(OrdStatus.REPLACED);
        replace.setExecType(ExecType.REPLACE);
        replace.setExecTranType(ExecTransType.NEW);
        replace.setLeavesQty(order.getOpen());
        replace.setCumQty(order.getExecuted());
        replace.setAvgPx(order.getAvgPx());
        order.setReceivedReplace(false);
        sendExecution(replace);
        ordersApi.update();
    }

    public void execute(Execution execution) {
        Order order = execution.getOrder();
        double fillQty = execution.getLastShares();
        double fillPrice = execution.getLastPx();
        double open = order.getOpen();
        // partial fill
        if (fillQty < open) {
            order.setOpen(open - fillQty);
            order.setStatus(OrdStatus.PARTIALLY_FILLED);
            execution.setExecType(ExecType.PARTIAL_FILL);
            // full or over execution
        } else {
            order.setOpen(0);
            order.setStatus(OrdStatus.FILLED);
            execution.setExecType(ExecType.FILL);
        }
        double avgPx = (order.getAvgPx() * order.getExecuted()
                + fillPrice * fillQty)
                / (order.getExecuted() + fillQty);
        order.setAvgPx(avgPx);
        order.setExecuted(order.getExecuted() + fillQty);
        ordersApi.update();
        // update execution
        execution.setExecTranType(ExecTransType.NEW);
        execution.setLeavesQty(order.getOpen());
        execution.setCumQty(order.getExecuted());
        execution.setAvgPx(avgPx);
        sendExecution(execution);
    }

    public void bust(Execution execution) {
        Execution bust = execution.clone();
        Order order = execution.getOrder();
        double fillQty = execution.getLastShares();
        double fillPrice = execution.getLastPx();
        double executed = order.getExecuted();
        // partial fill
        if (fillQty < executed) {
            order.setOpen(executed - fillQty);
            order.setStatus(OrdStatus.PARTIALLY_FILLED);
            double avgPx = (order.getAvgPx() * executed
                    - fillPrice * fillQty)
                    / (order.getExecuted() - fillQty);
            order.setAvgPx(avgPx);
            order.setExecuted(order.getExecuted() - fillQty);
            // full or over execution
        } else {
            order.setOpen(order.getQuantity());
            order.setStatus(OrdStatus.NEW);
            order.setAvgPx(0);
            order.setExecuted(0);
        }
        ordersApi.update();
        // update execution
        bust.setExecTranType(ExecTransType.CANCEL);
        bust.setLeavesQty(order.getOpen());
        bust.setCumQty(order.getExecuted());
        bust.setAvgPx(order.getAvgPx());
        sendExecution(bust);
    }

    public void correct(Execution correction) {
        Order order = correction.getOrder();
        Execution original = executionRepository.get(correction.getRefID());

        double fillQty = correction.getLastShares();
        double oldQty = original.getLastShares();

        double fillPrice = correction.getLastPx();
        double oldPrice = original.getLastPx();

        double executed = order.getExecuted();
        double ordered = order.getQuantity();

        double newCumQty = executed - oldQty + fillQty;
        double avgPx = (order.getAvgPx() * executed
                - oldPrice * oldQty
                + fillPrice * fillQty)
                / newCumQty;

        // partial fill
        if (newCumQty < ordered) {
            order.setOpen(ordered - newCumQty);
            order.setStatus(OrdStatus.PARTIALLY_FILLED);
            // full or over execution
        } else {
            order.setOpen(0);
            order.setStatus(OrdStatus.FILLED);
        }

        order.setAvgPx(avgPx);
        order.setExecuted(newCumQty);
        ordersApi.update();

        // update execution
        correction.setExecTranType(ExecTransType.CORRECT);
        correction.setLeavesQty(order.getOpen());
        correction.setCumQty(order.getExecuted());
        correction.setAvgPx(order.getAvgPx());
        sendExecution(correction);
    }

    // Message sending methods
    public void sendMessage(Message message) {
        String oboCompID = "<UNKNOWN>";
        String oboSubID = "<UNKNOWN>";
        boolean sendoboCompID = false;
        boolean sendoboSubID = false;

        try {
            oboCompID = settings.getString(currentSession, "OnBehalfOfCompID");
            oboSubID = settings.getString(currentSession, "OnBehalfOfSubID");
            sendoboCompID = settings.getBool("FIXimulatorSendOnBehalfOfCompID");
            sendoboSubID = settings.getBool("FIXimulatorSendOnBehalfOfSubID");
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }

        // Add OnBehalfOfCompID
        if (sendoboCompID && !oboCompID.equals("")) {
            OnBehalfOfCompID onBehalfOfCompID = new OnBehalfOfCompID(oboCompID);
            Header header = (Header) message.getHeader();
            header.set(onBehalfOfCompID);
        }

        // Add OnBehalfOfSubID
        if (sendoboSubID && !oboSubID.equals("")) {
            OnBehalfOfSubID onBehalfOfSubID = new OnBehalfOfSubID(oboSubID);
            Header header = (Header) message.getHeader();
            header.set(onBehalfOfSubID);
        }

        // Send actual message
        try {
            Session.sendToTarget(message, currentSession);
        } catch (SessionNotFound e) {
            e.printStackTrace();
        }
    }

    public void sendIOI(IOI ioi) {
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
                instrumentsApi.getInstrument(ioi.getSymbol());
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
        sendMessage(fixIOI);
        ioiRepository.add(ioi);
    }

    public void sendExecution(Execution execution) {
        Order order = execution.getOrder();

        // *** Required fields ***
        // OrderID (37)
        OrderID orderID = new OrderID(order.getID());

        // ExecID (17)
        ExecID execID = new ExecID(execution.id());

        // ExecTransType (20)
        ExecTransType execTransType =
                new ExecTransType(execution.getFIXExecTranType());

        // ExecType (150) Status of this report
        ExecType execType = new ExecType(execution.getFIXExecType());

        // OrdStatus (39) Status as a result of this report
        OrdStatus ordStatus =
                new OrdStatus(execution.getOrder().getFIXStatus());

        // Symbol (55)
        Symbol symbol = new Symbol(execution.getOrder().getSymbol());

        //  Side (54)
        Side side = new Side(execution.getOrder().getFIXSide());

        // LeavesQty ()
        LeavesQty leavesQty = new LeavesQty(execution.getLeavesQty());

        // CumQty ()
        CumQty cumQty = new CumQty(execution.getCumQty());

        // AvgPx ()
        AvgPx avgPx = new AvgPx(execution.getAvgPx());

        // Construct Execution Report from required fields
        quickfix.fix42.ExecutionReport executionReport =
                new quickfix.fix42.ExecutionReport(
                        orderID,
                        execID,
                        execTransType,
                        execType,
                        ordStatus,
                        symbol,
                        side,
                        leavesQty,
                        cumQty,
                        avgPx);

        // *** Conditional fields ***
        if (execution.getRefID() != null) {
            executionReport.set(
                    new ExecRefID(execution.getRefID()));
        }

        // *** Optional fields ***
        executionReport.set(new ClOrdID(execution.getOrder().getClientOrderID()));
        executionReport.set(new OrderQty(execution.getOrder().getQuantity()));
        executionReport.set(new LastShares(execution.getLastShares()));
        executionReport.set(new LastPx(execution.getLastPx()));
        System.out.println("Setting...");
        System.out.println("SecurityID: " + order.getSecurityID());
        System.out.println("IDSource: " + order.getIdSource());
        if (order.getSecurityID() != null
                && order.getIdSource() != null) {
            executionReport.set(new SecurityID(order.getSecurityID()));
            executionReport.set(new IDSource(order.getIdSource()));
        }

        // *** Send message ***
        sendMessage(executionReport);
        executionRepository.add(execution);
    }

    // IOI Sender methods
    public void startIOIsender(Integer delay, String symbol, String securityID) {
        try {
            ioiSender = new IOIsender(delay, symbol, securityID);
            ioiSenderThread = new Thread(ioiSender);
            ioiSenderThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (connected && ioiSenderStarted)
            ioiSenderStatus.on();
    }

    public void stopIOIsender() {
        ioiSender.stopIOISender();
        ioiSenderThread.interrupt();
        try {
            ioiSenderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ioiSenderStatus.off();
    }

    public void setNewDelay(Integer delay) {
        if (ioiSenderStarted) ioiSender.setDelay(delay);
    }

    public void setNewSymbol(String identifier) {
        if (ioiSenderStarted) ioiSender.setSymbol(identifier);
    }

    public void setNewSecurityID(String identifier) {
        if (ioiSenderStarted) ioiSender.setSecurityID(identifier);
    }

    public class IOIsender implements Runnable {
        private Integer delay;
        private String symbolValue = "";
        private String securityIDvalue = "";

        public IOIsender(Integer delay, String symbol, String securityID) {
            ioiSenderStarted = true;
            this.delay = delay;
            symbolValue = symbol;
            securityIDvalue = securityID;
        }

        public void stopIOISender() {
            ioiSenderStarted = false;
        }

        public void setDelay(Integer delay) {
            this.delay = delay;
        }

        public void setSymbol(String identifier) {
            symbolValue = identifier;
        }

        public void setSecurityID(String identifier) {
            securityIDvalue = identifier;
        }

        public void run() {
            while (connected && ioiSenderStarted) {
                sendRandomIOI();
                try {
                    Thread.sleep(delay.longValue());
                } catch (InterruptedException e) {
                    LOG.error("Error: ", e);
                }
            }
            ioiSenderStatus.off();
        }

        public void sendRandomIOI() {
            Instrument instrument = instrumentsApi.randomInstrument();
            IOI ioi = new IOI();
            ioi.setType("NEW");

            // Side
            ioi.setSide("BUY");
            if (random.nextBoolean()) ioi.setSide("SELL");

            // IOIShares
            Integer quantity = random.nextInt(1000) * 100 + 100;
            ioi.setQuantity(quantity);

            // Symbol
            String value = "";
            if (symbolValue.equals("Ticker")) value = instrument.getTicker();
            if (symbolValue.equals("RIC")) value = instrument.getRIC();
            if (symbolValue.equals("Sedol")) value = instrument.getSedol();
            if (symbolValue.equals("Cusip")) value = instrument.getCusip();
            if (value.equals("")) value = "<MISSING>";
            ioi.setSymbol(value);

            // *** Optional fields ***
            // SecurityID
            value = "";
            if (securityIDvalue.equals("Ticker"))
                value = instrument.getTicker();
            if (securityIDvalue.equals("RIC"))
                value = instrument.getRIC();
            if (securityIDvalue.equals("Sedol"))
                value = instrument.getSedol();
            if (securityIDvalue.equals("Cusip"))
                value = instrument.getCusip();
            if (value.equals(""))
                value = "<MISSING>";
            ioi.setSecurityID(value);

            // IDSource
            if (securityIDvalue.equals("Ticker")) ioi.setIDSource("TICKER");
            if (securityIDvalue.equals("RIC")) ioi.setIDSource("RIC");
            if (securityIDvalue.equals("Sedol")) ioi.setIDSource("SEDOL");
            if (securityIDvalue.equals("Cusip")) ioi.setIDSource("CUSIP");
            if (ioi.getSecurityID().equals("<MISSING>"))
                ioi.setIDSource("UNKNOWN");

            // Price
            int pricePrecision = 4;
            try {
                pricePrecision =
                        (int) settings.getLong("FIXimulatorPricePrecision");
            } catch (Exception e) {
                LOG.error("Error: ", e);
            }
            double factor = Math.pow(10, pricePrecision);
            double price = Math.round(
                    random.nextDouble() * 100 * factor) / factor;
            ioi.setPrice(price);

            // IOINaturalFlag
            ioi.setNatural("No");
            if (random.nextBoolean()) ioi.setNatural("Yes");

            sendIOI(ioi);
        }
    }

    // Executor methods
    public void startExecutor(Integer delay, Integer partials) {
        try {
            executor = new Executor(delay, partials);
            executorThread = new Thread(executor);
            executorThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (connected && executorStarted)
            executorStatus.on();
    }

    public void stopExecutor() {
        executor.stopExecutor();
        executorThread.interrupt();
        try {
            executorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorStatus.off();
    }

    public void setNewExecutorDelay(Integer delay) {
        if (executorStarted) executor.setDelay(delay);
    }

    public void setNewExecutorPartials(Integer partials) {
        if (executorStarted) executor.setPartials(partials);
    }

    public class Executor implements Runnable {
        private Integer delay;
        private Integer partials;

        public Executor(Integer delay, Integer partials) {
            executorStarted = true;
            this.partials = partials;
            this.delay = delay;
        }

        public void run() {
            while (connected && executorStarted) {
                while (ordersApi.haveOrdersToFill()) {
                    Order order = ordersApi.getOrderToFill();
                    acknowledge(order);
                    fill(order);
                }
                // No orders to fill, check again in 5 seconds
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    LOG.error("Error: ", e);
                }
            }
            executorStatus.off();
        }

        public void stopExecutor() {
            executorStarted = false;
        }

        public void setDelay(Integer delay) {
            this.delay = delay;
        }

        public void setPartials(Integer partials) {
            this.partials = partials;
        }

        public void fill(Order order) {
            double fillQty = Math.floor(order.getQuantity() / partials);
            double fillPrice;
            // try to look the price up from the instruments
            Instrument instrument =
                    instrumentsApi.getInstrument(order.getSymbol());
            if (instrument != null) {
                fillPrice = Double.valueOf(instrument.getPrice());
                // use a random price
            } else {
                int pricePrecision = 4;
                try {
                    pricePrecision =
                            (int) settings.getLong("FIXimulatorPricePrecision");
                } catch (Exception e) {
                    LOG.error("Error: ", e);
                }
                double factor = Math.pow(10, pricePrecision);
                fillPrice = Math.round(
                        random.nextDouble() * 100 * factor) / factor;
            }

            if (fillQty == 0) fillQty = 1;
            for (int i = 0; i < partials; i++) {
                double open = order.getOpen();
                if (open > 0) {
                    if (fillQty < open && i != partials - 1) {
                        // send partial
                        // calculate fields
                        double priorQty = order.getExecuted();
                        double priorAvg = order.getAvgPx();
                        if (random.nextBoolean())
                            fillPrice += 0.01;
                        else
                            fillPrice -= 0.01;
                        double thisAvg = ((fillQty * fillPrice)
                                + (priorQty * priorAvg))
                                / (priorQty + fillQty);
                        int pricePrecision = 4;
                        try {
                            pricePrecision =
                                    (int) settings.getLong("FIXimulatorPricePrecision");
                        } catch (Exception e) {
                            LOG.error("Error: ", e);
                        }
                        double factor = Math.pow(10, pricePrecision);
                        fillPrice = Math.round(fillPrice * factor) / factor;
                        thisAvg = Math.round(thisAvg * factor) / factor;
                        // update order
                        order.setOpen(open - fillQty);
                        order.setStatus(OrdStatus.PARTIALLY_FILLED);
                        order.setExecuted(order.getExecuted() + fillQty);
                        order.setAvgPx(thisAvg);
                        ordersApi.update();
                        // create execution
                        Execution partial = new Execution(order);
                        partial.setExecType(ExecType.PARTIAL_FILL);
                        partial.setExecTranType(ExecTransType.NEW);
                        partial.setLeavesQty(order.getOpen());
                        partial.setCumQty(order.getQuantity() - order.getOpen());
                        partial.setAvgPx(thisAvg);
                        partial.setLastShares(fillQty);
                        partial.setLastPx(fillPrice);
                        sendExecution(partial);
                    } else {
                        // send full
                        fillQty = open;
                        // calculate fields
                        double priorQty = order.getExecuted();
                        double priorAvg = order.getAvgPx();
                        if (random.nextBoolean())
                            fillPrice += 0.01;
                        else
                            fillPrice -= 0.01;
                        double thisAvg = ((fillQty * fillPrice)
                                + (priorQty * priorAvg))
                                / (priorQty + fillQty);
                        int pricePrecision = 4;
                        try {
                            pricePrecision =
                                    (int) settings.getLong("FIXimulatorPricePrecision");
                        } catch (Exception e) {
                            LOG.error("Error: ", e);
                        }
                        double factor = Math.pow(10, pricePrecision);
                        fillPrice = Math.round(fillPrice * factor) / factor;
                        thisAvg = Math.round(thisAvg * factor) / factor;
                        //update order
                        order.setOpen(open - fillQty);
                        order.setStatus(OrdStatus.FILLED);
                        order.setExecuted(order.getExecuted() + fillQty);
                        order.setAvgPx(thisAvg);
                        ordersApi.update();
                        // create execution
                        Execution partial = new Execution(order);
                        partial.setExecType(ExecType.FILL);
                        partial.setExecTranType(ExecTransType.NEW);
                        partial.setLeavesQty(order.getOpen());
                        partial.setCumQty(order.getQuantity() - order.getOpen());
                        partial.setAvgPx(thisAvg);
                        partial.setLastShares(fillQty);
                        partial.setLastPx(fillPrice);
                        sendExecution(partial);
                        break;
                    }
                }
                try {
                    Thread.sleep(delay.longValue());
                } catch (InterruptedException e) {
                    LOG.error("Error: ", e);
                }
            }
        }
    }
}
