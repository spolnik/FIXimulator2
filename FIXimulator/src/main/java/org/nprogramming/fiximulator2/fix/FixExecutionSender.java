package org.nprogramming.fiximulator2.fix;

import com.wordpress.nprogramming.oms.api.Execution;
import org.nprogramming.fiximulator2.api.NotifyService;
import org.nprogramming.fiximulator2.api.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.field.*;
import quickfix.fix42.ExecutionReport;

final class FixExecutionSender {

    private final FixMessageSender fixMessageSender;
    private final Repository<Execution> repository;
    private final NotifyService notifyService;

    private static final Logger LOG = LoggerFactory.getLogger(FixExecutionSender.class);

    public FixExecutionSender(
            FixMessageSender fixMessageSender,
            Repository<Execution> repository,
            NotifyService notifyService
    ) {
        this.fixMessageSender = fixMessageSender;
        this.repository = repository;
        this.notifyService = notifyService;
    }

    public void send(Execution execution) {

        // *** Required fields ***
        // OrderID (37)
        OrderID orderID = new OrderID(execution.getOrderId());

        // ExecID (17)
        ExecID execID = new ExecID(execution.id());

        // ExecTransType (20)
        ExecTransType execTransType =
                new ExecTransType(execution.getFIXExecTranType());

        // ExecType (150) Status of this report
        ExecType execType = new ExecType(execution.getFIXExecType());

        // OrdStatus (39) Status as a result of this report
        OrdStatus ordStatus =
                new OrdStatus(execution.getFIXStatus());

        // Symbol (55)
        Symbol symbol = new Symbol(execution.getSymbol());

        //  Side (54)
        Side side = new Side(execution.getFIXSide());

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
        executionReport.set(new ClOrdID(execution.getClientOrderID()));
        executionReport.set(new OrderQty(execution.getOrderQuantity()));
        executionReport.set(new LastShares(execution.getLastShares()));
        executionReport.set(new LastPx(execution.getLastPx()));

        LOG.debug("Setting...");
        LOG.debug("SecurityID: " + execution.getSecurityID());
        LOG.debug("IDSource: " + execution.getIdSource());

        if (execution.getSecurityID() != null
                && execution.getIdSource() != null) {
            executionReport.set(new SecurityID(execution.getSecurityID()));
            executionReport.set(new IDSource(execution.getIdSource()));
        }

        sendAndSave(execution, executionReport);
    }

    private void sendAndSave(Execution execution, ExecutionReport executionReport) {
        fixMessageSender.send(executionReport);
        repository.save(execution);
        notifyService.sendChangedExecutionId(execution.id());
    }
}
