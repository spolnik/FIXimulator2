package org.nprogramming.fiximulator2.fix;

import org.nprogramming.fiximulator2.api.RepositoryWithCallback;
import org.nprogramming.fiximulator2.domain.Execution;
import org.nprogramming.fiximulator2.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.field.*;
import quickfix.fix42.ExecutionReport;

final class FixExecutionSender {

    private final FixMessageSender fixMessageSender;
    private final RepositoryWithCallback<Execution> repository;

    private static final Logger LOG = LoggerFactory.getLogger(FixExecutionSender.class);

    public FixExecutionSender(
            FixMessageSender fixMessageSender,
            RepositoryWithCallback<Execution> repository) {
        this.fixMessageSender = fixMessageSender;
        this.repository = repository;
    }

    public void send(Execution execution) {
        Order order = execution.getOrder();

        // *** Required fields ***
        // OrderID (37)
        OrderID orderID = new OrderID(order.id());

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

        LOG.debug("Setting...");
        LOG.debug("SecurityID: " + order.getSecurityID());
        LOG.debug("IDSource: " + order.getIdSource());

        if (order.getSecurityID() != null
                && order.getIdSource() != null) {
            executionReport.set(new SecurityID(order.getSecurityID()));
            executionReport.set(new IDSource(order.getIdSource()));
        }

        sendAndSave(execution, executionReport);
    }

    private void sendAndSave(Execution execution, ExecutionReport executionReport) {
        fixMessageSender.send(executionReport);
        repository.save(execution);
    }
}
