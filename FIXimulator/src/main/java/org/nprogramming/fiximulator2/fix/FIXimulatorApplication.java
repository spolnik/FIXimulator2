package org.nprogramming.fiximulator2.fix;

import com.wordpress.nprogramming.instruments.api.Instrument;
import com.wordpress.nprogramming.instruments.api.InstrumentsRepository;
import com.wordpress.nprogramming.oms.api.Execution;
import com.wordpress.nprogramming.oms.api.IOI;
import com.wordpress.nprogramming.oms.api.Order;
import org.nprogramming.fiximulator2.api.MessageHandler;
import org.nprogramming.fiximulator2.api.NotifyService;
import org.nprogramming.fiximulator2.data.OrdersRepository;
import com.wordpress.nprogramming.oms.api.Repository;
import org.nprogramming.fiximulator2.api.event.ConnectionStatus;
import org.nprogramming.fiximulator2.api.event.ExecutionChanged;
import org.nprogramming.fiximulator2.api.event.OrderChanged;
import org.nprogramming.fiximulator2.log4fix.LogMessageSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix42.OrderCancelReject;

import java.io.*;
import java.util.List;
import java.util.Random;

public class FIXimulatorApplication extends MessageCracker
        implements Application {

    private static final Logger LOG = LoggerFactory.getLogger(FIXimulatorApplication.class);

    private final NotifyService notifyService;

    private boolean connected;
    private MessageHandler<ConnectionStatus> connectedStatus;
    private MessageHandler<ConnectionStatus> ioiSenderStatus;
    private MessageHandler<ConnectionStatus> executorStatus;
    private boolean ioiSenderStarted;
    private boolean executorStarted;
    private IOISender ioiSender;
    private Thread ioiSenderThread;
    private Executor executor;
    private Thread executorThread;
    private LogMessageSet messages;
    private final OrdersRepository ordersRepository;
    private final Repository<Execution> executionRepository;
    private final Repository<IOI> ioiRepository;
    private final InstrumentsRepository instrumentsRepository;
    private final OrderFixTranslator orderFixTranslator;
    private SessionSettings settings;
    private SessionID currentSession;
    private DataDictionary dictionary;
    private Random random = new Random();
    private FixMessageSender fixMessageSender;
    private FixExecutionSender fixExecutionSender;
    private FixIOISender fixIOISender;

    public FIXimulatorApplication(
            SessionSettings settings,
            LogMessageSet messages,
            OrdersRepository ordersRepository,
            Repository<Execution> executionRepository,
            Repository<IOI> ioiRepository,
            InstrumentsRepository instrumentsRepository,
            OrderFixTranslator orderFixTranslator,
            NotifyService notifyService) {
        this.settings = settings;
        this.messages = messages;
        this.ordersRepository = ordersRepository;
        this.executionRepository = executionRepository;
        this.ioiRepository = ioiRepository;
        this.instrumentsRepository = instrumentsRepository;
        this.orderFixTranslator = orderFixTranslator;
        this.notifyService = notifyService;
    }

    @Override
    public void onCreate(SessionID sessionID) {
    }

    @Override
    public void onLogon(SessionID sessionID) {
        connected = true;
        currentSession = sessionID;
        dictionary = Session.lookupSession(currentSession).getDataDictionary();
        if (connectedStatus != null)
            connectedStatus.onMessage(ConnectionStatus.on());

        fixMessageSender = new FixMessageSender(settings, sessionID);
        fixExecutionSender = new FixExecutionSender(fixMessageSender, executionRepository, notifyService);
        fixIOISender = new FixIOISender(fixMessageSender, instrumentsRepository, ioiRepository, notifyService);
    }

    @Override
    public void onLogout(SessionID sessionID) {
        connected = false;
        currentSession = null;
        fixMessageSender = null;
        connectedStatus.onMessage(ConnectionStatus.off());
    }

    public void onMessage(quickfix.fix42.IndicationofInterest message,
                          SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
    }

    public void onMessage(quickfix.fix42.NewOrderSingle message,
                          SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        Order order = orderFixTranslator.from(message);
        order.setReceivedOrder(true);
        if (executorStarted) {
            ordersRepository.addOrderToFill(order);
            notifyService.send(OrderChanged.class, OrderChanged.withId(order.id()));
            executorThread.interrupt();
        } else {
            ordersRepository.save(order);
            notifyService.send(OrderChanged.class, OrderChanged.withId(order.id()));
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

    public void onMessage(quickfix.fix42.OrderCancelRequest message,
                          SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        Order order = orderFixTranslator.from(message);
        order.setReceivedCancel(true);
        ordersRepository.save(order);
        notifyService.send(OrderChanged.class, OrderChanged.withId(order.id()));
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

    public void onMessage(quickfix.fix42.OrderCancelReplaceRequest message,
                          SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
        Order order = orderFixTranslator.from(message);
        order.setReceivedReplace(true);
        ordersRepository.save(order);
        notifyService.send(OrderChanged.class, OrderChanged.withId(order.id()));
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

    public void onMessage(quickfix.fix42.OrderCancelReject message,
                          SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
    }

    public void onMessage(quickfix.fix42.ExecutionReport message,
                          SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
    }

    public void onMessage(quickfix.fix42.DontKnowTrade message,
                          SessionID sessionID)
            throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {

        try {
            ExecID execID = new ExecID();
            message.get(execID);
            Execution execution =
                    executionRepository.queryById(execID.getValue());
            execution.setDKd(true);
            notifyService.send(ExecutionChanged.class, ExecutionChanged.withId(execution.id()));
        } catch (FieldNotFound ex) {
            LOG.error("Error: ", ex);
        }
    }

    @Override
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
            LOG.error("Error: ", e);
        }
    }

    public void fromAdmin(Message message, SessionID sessionID)
            throws
            FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
    }

    public void toAdmin(Message message, SessionID sessionID) {
    }

    public void addStatusCallbacks(
            MessageHandler<ConnectionStatus> connectedStatus,
            MessageHandler<ConnectionStatus> ioiSenderStatus,
            MessageHandler<ConnectionStatus> executorStatus
    ) {
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
            LOG.error("Error: ", ex);
        }
    }

    public void acknowledge(Order order) {
        Execution acknowledgement = Execution.createFrom(order);
        order.setFixStatus(OrdStatus.NEW);
        acknowledgement.setFixExecType(ExecType.NEW);
        acknowledgement.setFixExecTranType(ExecTransType.NEW);
        acknowledgement.setLeavesQty(order.getOpen());
        fixExecutionSender.send(acknowledgement);
        order.setReceivedOrder(false);
        notifyService.send(OrderChanged.class, OrderChanged.withId(order.id()));
    }

    public void reject(Order order) {
        Execution reject = Execution.createFrom(order);
        order.setFixStatus(OrdStatus.REJECTED);
        reject.setFixExecType(ExecType.REJECTED);
        reject.setFixExecTranType(ExecTransType.NEW);
        reject.setLeavesQty(order.getOpen());
        fixExecutionSender.send(reject);
        order.setReceivedOrder(false);
        notifyService.send(OrderChanged.class, OrderChanged.withId(order.id()));
    }

    public void dfd(Order order) {
        Execution dfd = Execution.createFrom(order);
        order.setFixStatus(OrdStatus.DONE_FOR_DAY);
        dfd.setFixExecType(ExecType.DONE_FOR_DAY);
        dfd.setFixExecTranType(ExecTransType.NEW);
        dfd.setLeavesQty(order.getOpen());
        dfd.setCumQty(order.getExecuted());
        dfd.setAvgPx(order.getAvgPx());
        fixExecutionSender.send(dfd);
        notifyService.send(OrderChanged.class, OrderChanged.withId(order.id()));
    }

    public void pendingCancel(Order order) {
        Execution pending = Execution.createFrom(order);
        order.setFixStatus(OrdStatus.PENDING_CANCEL);
        pending.setFixExecType(ExecType.PENDING_CANCEL);
        pending.setFixExecTranType(ExecTransType.NEW);
        pending.setLeavesQty(order.getOpen());
        pending.setCumQty(order.getExecuted());
        pending.setAvgPx(order.getAvgPx());
        fixExecutionSender.send(pending);
        order.setReceivedCancel(false);
        notifyService.send(OrderChanged.class, OrderChanged.withId(order.id()));
    }

    public void cancel(Order order) {
        Execution cancel = Execution.createFrom(order);
        order.setFixStatus(OrdStatus.CANCELED);
        cancel.setFixExecType(ExecType.CANCELED);
        cancel.setFixExecTranType(ExecTransType.NEW);
        cancel.setLeavesQty(order.getOpen());
        cancel.setCumQty(order.getExecuted());
        cancel.setAvgPx(order.getAvgPx());
        fixExecutionSender.send(cancel);
        order.setReceivedCancel(false);
        notifyService.send(OrderChanged.class, OrderChanged.withId(order.id()));
    }

    public void rejectCancelReplace(Order order, boolean cancel) {
        quickfix.fix42.OrderCancelReject rejectMessage = rejectCancelReplaceOrder(order, cancel);

        fixMessageSender.send(rejectMessage);
        notifyService.send(OrderChanged.class, OrderChanged.withId(order.id()));
    }

    private OrderCancelReject rejectCancelReplaceOrder(Order order, boolean cancel) {
        order.setReceivedCancel(false);
        order.setReceivedReplace(false);
        // *** Required fields ***
        // OrderID (37)
        OrderID orderID = new OrderID(order.id());

        ClOrdID clientID = new ClOrdID(order.getClientOrderID());

        OrigClOrdID origClientID = new OrigClOrdID(order.getOrigClientID());

        // OrdStatus (39) Status as a result of this report
        if (order.getStatus().equals("<UNKNOWN>"))
            order.setFixStatus(OrdStatus.NEW);
        OrdStatus ordStatus = new OrdStatus(order.getFixStatus());

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
        Execution pending = Execution.createFrom(order);
        order.setFixStatus(OrdStatus.PENDING_REPLACE);
        pending.setFixExecType(ExecType.PENDING_REPLACE);
        pending.setFixExecTranType(ExecTransType.NEW);
        pending.setLeavesQty(order.getOpen());
        pending.setCumQty(order.getExecuted());
        pending.setAvgPx(order.getAvgPx());
        order.setReceivedReplace(false);
        fixExecutionSender.send(pending);
        notifyService.send(OrderChanged.class, OrderChanged.withId(order.id()));
    }

    public void replace(Order order) {
        Execution replace = Execution.createFrom(order);
        order.setFixStatus(OrdStatus.REPLACED);
        replace.setFixExecType(ExecType.REPLACE);
        replace.setFixExecTranType(ExecTransType.NEW);
        replace.setLeavesQty(order.getOpen());
        replace.setCumQty(order.getExecuted());
        replace.setAvgPx(order.getAvgPx());
        order.setReceivedReplace(false);
        fixExecutionSender.send(replace);
        notifyService.send(OrderChanged.class, OrderChanged.withId(order.id()));
    }

    public void execute(Execution execution) {
        double fillQty = execution.getLastShares();
        double fillPrice = execution.getLastPx();
        double open = execution.getOpen();
        // partial fill
        if (fillQty < open) {
            execution.setOpen(open - fillQty);
            execution.setFixStatus(OrdStatus.PARTIALLY_FILLED);
            execution.setFixExecType(ExecType.PARTIAL_FILL);
            // full or over execution
        } else {
            execution.setOpen(0);
            execution.setFixStatus(OrdStatus.FILLED);
            execution.setFixExecType(ExecType.FILL);
        }
        double avgPx = (execution.getAvgPx() * execution.getExecuted()
                + fillPrice * fillQty)
                / (execution.getExecuted() + fillQty);
        execution.setAvgPx(avgPx);
        execution.setExecuted(execution.getExecuted() + fillQty);
        notifyService.send(OrderChanged.class, OrderChanged.withId(execution.getOrderId()));
        // update execution
        execution.setFixExecTranType(ExecTransType.NEW);
        execution.setLeavesQty(execution.getOpen());
        execution.setCumQty(execution.getExecuted());
        execution.setAvgPx(avgPx);
        fixExecutionSender.send(execution);
    }

    public void bust(Execution execution) {
        Execution bust = execution.clone();

        double fillQty = execution.getLastShares();
        double fillPrice = execution.getLastPx();
        double executed = execution.getExecuted();

        // partial fill
        if (fillQty < executed) {
            execution.setOpen(executed - fillQty);
            execution.setFixStatus(OrdStatus.PARTIALLY_FILLED);
            double avgPx = (execution.getAvgPx() * executed
                    - fillPrice * fillQty)
                    / (execution.getExecuted() - fillQty);
            execution.setAvgPx(avgPx);
            execution.setExecuted(execution.getExecuted() - fillQty);
            // full or over execution
        } else {
            execution.setOpen(execution.getOrderQuantity());
            execution.setFixStatus(OrdStatus.NEW);
            execution.setAvgPx(0);
            execution.setExecuted(0);
        }
        notifyService.send(OrderChanged.class, OrderChanged.withId(execution.getOrderId()));
        // update execution
        bust.setFixExecTranType(ExecTransType.CANCEL);
        bust.setLeavesQty(execution.getOpen());
        bust.setCumQty(execution.getExecuted());
        bust.setAvgPx(execution.getAvgPx());
        fixExecutionSender.send(bust);
    }

    public void correct(Execution correction) {
        Execution original = executionRepository.queryById(correction.getRefID());

        double fillQty = correction.getLastShares();
        double oldQty = original.getLastShares();

        double fillPrice = correction.getLastPx();
        double oldPrice = original.getLastPx();

        double executed = original.getExecuted();
        double ordered = original.getOrderQuantity();

        double newCumQty = executed - oldQty + fillQty;
        double avgPx = (original.getAvgPx() * executed
                - oldPrice * oldQty
                + fillPrice * fillQty)
                / newCumQty;

        // partial fill
        if (newCumQty < ordered) {
            original.setOpen(ordered - newCumQty);
            original.setFixStatus(OrdStatus.PARTIALLY_FILLED);
            // full or over execution
        } else {
            original.setOpen(0);
            original.setFixStatus(OrdStatus.FILLED);
        }

        original.setAvgPx(avgPx);
        original.setExecuted(newCumQty);
        notifyService.send(OrderChanged.class, OrderChanged.withId(original.getOrderId()));

        // update execution
        correction.setFixExecTranType(ExecTransType.CORRECT);
        correction.setLeavesQty(original.getOpen());
        correction.setCumQty(original.getExecuted());
        correction.setAvgPx(original.getAvgPx());
        fixExecutionSender.send(correction);
    }

    // IOI Sender methods
    public void startIOIsender(Integer delay, String symbol, String securityID) {
        try {
            ioiSender = new IOISender(delay, symbol, securityID);
            ioiSenderThread = new Thread(ioiSender);
            ioiSenderThread.start();
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
        if (connected && ioiSenderStarted)
            ioiSenderStatus.onMessage(ConnectionStatus.on());
    }

    public void stopIOIsender() {
        ioiSender.stopIOISender();
        ioiSenderThread.interrupt();
        try {
            ioiSenderThread.join();
        } catch (InterruptedException e) {
            LOG.error("Error: ", e);
        }
        ioiSenderStatus.onMessage(ConnectionStatus.off());
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

    public void sendIOI(IOI ioi) {
        fixIOISender.send(ioi);
    }

    public class IOISender implements Runnable {
        private Integer delay;
        private String symbolValue = "";
        private String securityIDvalue = "";

        public IOISender(Integer delay, String symbol, String securityID) {
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
            ioiSenderStatus.onMessage(ConnectionStatus.off());
        }

        private Instrument randomInstrument() {

            List<Instrument> instruments = instrumentsRepository.getAll();
            Random generator = new Random();

            int size = instruments.size();
            int index = generator.nextInt(size);

            return instruments.get(index);
        }

        public void sendRandomIOI() {
            Instrument instrument = randomInstrument();
            IOI ioi = new IOI(IOI.generateID());
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
            if (random.nextBoolean())
                ioi.setNatural("Yes");

            fixIOISender.send(ioi);
        }
    }

    // Executor methods
    public void startExecutor(Integer delay, Integer partials) {
        try {
            executor = new Executor(delay, partials);
            executorThread = new Thread(executor);
            executorThread.start();
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
        if (connected && executorStarted)
            executorStatus.onMessage(ConnectionStatus.on());
    }

    public void stopExecutor() {
        executor.stopExecutor();
        executorThread.interrupt();
        try {
            executorThread.join();
        } catch (InterruptedException e) {
            LOG.error("Error: ", e);
        }
        executorStatus.onMessage(ConnectionStatus.off());
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
                while (ordersRepository.haveOrdersToFill()) {
                    Order order = ordersRepository.getOrderToFill();
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
            executorStatus.onMessage(ConnectionStatus.off());
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
                    instrumentsRepository.queryById(order.getSymbol());
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

            if (Double.doubleToRawLongBits(fillQty) == 0)
                fillQty = 1;

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
                        order.setFixStatus(OrdStatus.PARTIALLY_FILLED);
                        order.setExecuted(order.getExecuted() + fillQty);
                        order.setAvgPx(thisAvg);
                        notifyService.send(OrderChanged.class, OrderChanged.withId(order.id()));
                        // create execution
                        Execution partial = Execution.createFrom(order);
                        partial.setFixExecType(ExecType.PARTIAL_FILL);
                        partial.setFixExecTranType(ExecTransType.NEW);
                        partial.setLeavesQty(order.getOpen());
                        partial.setCumQty(order.getQuantity() - order.getOpen());
                        partial.setAvgPx(thisAvg);
                        partial.setLastShares(fillQty);
                        partial.setLastPx(fillPrice);
                        fixExecutionSender.send(partial);
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
                        order.setFixStatus(OrdStatus.FILLED);
                        order.setExecuted(order.getExecuted() + fillQty);
                        order.setAvgPx(thisAvg);
                        notifyService.send(OrderChanged.class, OrderChanged.withId(order.id()));
                        // create execution
                        Execution partial = Execution.createFrom(order);
                        partial.setFixExecType(ExecType.FILL);
                        partial.setFixExecTranType(ExecTransType.NEW);
                        partial.setLeavesQty(order.getOpen());
                        partial.setCumQty(order.getQuantity() - order.getOpen());
                        partial.setAvgPx(thisAvg);
                        partial.setLastShares(fillQty);
                        partial.setLastPx(fillPrice);
                        fixExecutionSender.send(partial);
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
