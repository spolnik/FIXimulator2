package org.nprogramming.fiximulator2.ui.tables;

import org.nprogramming.fiximulator2.api.MessageHandler;
import org.nprogramming.fiximulator2.api.NotifyService;
import org.nprogramming.fiximulator2.api.Repository;
import com.wordpress.nprogramming.oms.api.Execution;
import com.wordpress.nprogramming.oms.api.Order;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.Map;

public class ExecutionTableModel extends AbstractTableModel implements MessageHandler {

    private static final int ID = 0;
    private static final int CLIENT_ORDER_ID = 1;
    private static final int SIDE = 2;
    private static final int SYMBOL = 3;
    private static final int LAST_QTY = 4;
    private static final int LAST_PX = 5;
    private static final int CUM_QTY = 6;
    private static final int AVG_PX = 7;
    private static final int OPEN = 8;
    private static final int EXEC_TYPE = 9;
    private static final int EXEC_TRAN_TYPE = 10;
    private static final int REF_ID = 11;
    private static final int DKD = 12;

    private static final String[] headers =
            {"ID", "ClOrdID", "Side", "Symbol", "LastQty", "LastPx",
                    "CumQty", "AvgPx", "Open", "ExecType", "ExecTranType", "RefID", "DKd"};

    private final transient Map<Integer, Execution> rowToExecution;
    private final transient Map<String, Integer> idToRow;

    private final transient Repository<Execution> executionsRepository;

    public ExecutionTableModel(Repository<Execution> executionsRepository, NotifyService notifyService) {
        this.executionsRepository = executionsRepository;

        rowToExecution = new HashMap<>();
        idToRow = new HashMap<>();

        executionsRepository.getAll().forEach(
                this::addOrReplaceAndRefresh
        );

        notifyService.addExecutionMessageHandler(this);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
    }

    @Override
    public int getColumnCount() {
        return headers.length;
    }

    @Override
    public String getColumnName(int column) {
        return headers[column];
    }

    @Override
    public Class getColumnClass(int column) {

        switch (column) {
            case LAST_QTY:
            case LAST_PX:
            case CUM_QTY:
            case AVG_PX:
            case OPEN:
                return Double.class;
            case DKD:
                return Boolean.class;
            default:
                return String.class;
        }
    }

    @Override
    public int getRowCount() {
        return rowToExecution.size();
    }

    @Override
    public Object getValueAt(int row, int column) {

        Execution execution = get(row);
        Order order = execution.getOrder();

        switch (column) {
            case ID:
                return execution.id();
            case CLIENT_ORDER_ID:
                return order.getClientOrderID();
            case SIDE:
                return order.getSide();
            case SYMBOL:
                return order.getSymbol();
            case LAST_QTY:
                return execution.getLastShares();
            case LAST_PX:
                return execution.getLastPx();
            case CUM_QTY:
                return execution.getCumQty();
            case AVG_PX:
                return execution.getAvgPx();
            case OPEN:
                return order.getOpen();
            case EXEC_TYPE:
                return execution.getExecType();
            case EXEC_TRAN_TYPE:
                return execution.getExecTranType();
            case REF_ID:
                return execution.getRefID();
            case DKD:
                return execution.isDKd();
            default:
                return "";
        }
    }

    @Override
    public void onMessage(String id) {
        addOrReplaceAndRefresh(
                executionsRepository.get(id)
        );
    }

    public Execution get(int row) {
        return rowToExecution.get(row);
    }

    private void addAndRefresh(Execution execution) {
        int row = rowToExecution.size();

        rowToExecution.put(row, execution);
        idToRow.put(execution.id(), row);

        fireTableRowsInserted(row, row);
    }

    private void addOrReplaceAndRefresh(Execution execution) {

        Integer row = idToRow.get(execution.id());

        if (row == null) {
            addAndRefresh(execution);
            return;
        }

        rowToExecution.put(row, execution);
        idToRow.put(execution.id(), row);

        fireTableRowsUpdated(row, row);
    }
}
