package org.nprogramming.fiximulator2.ui.tables;

import org.nprogramming.fiximulator2.api.ExecutionsApi;
import org.nprogramming.fiximulator2.api.NotifyApi;
import org.nprogramming.fiximulator2.domain.Execution;
import org.nprogramming.fiximulator2.domain.Order;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.Map;

public class ExecutionTableModel extends AbstractTableModel implements NotifyApi {

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

    private final Map<Integer, Execution> rowToExecution;
    private final Map<String, Integer> idToRow;

    private static String[] headers =
            {"ID", "ClOrdID", "Side", "Symbol", "LastQty", "LastPx",
                    "CumQty", "AvgPx", "Open", "ExecType", "ExecTranType", "RefID", "DKd"};

    private final ExecutionsApi executionsApi;

    public ExecutionTableModel(ExecutionsApi executionsApi) {
        this.executionsApi = executionsApi;

        rowToExecution = new HashMap<>();
        idToRow = new HashMap<>();

        executionsApi.getAll().forEach(
                this::addExecution
        );

        executionsApi.addCallback(this);
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
            default:
                return String.class;
        }
    }

    @Override
    public int getRowCount() {
        return rowToExecution.size();
    }

    private void addExecution(Execution execution) {
        int row = rowToExecution.size();

        rowToExecution.put(row, execution);
        idToRow.put(execution.getID(), row);

        fireTableRowsInserted(row, row);
    }

    private Execution getExecution(int row) {
        return rowToExecution.get(row);
    }

    @Override
    public Object getValueAt(int row, int column) {

        Execution execution = getExecution(row);
        Order order = execution.getOrder();

        switch (column) {
            case ID:
                return execution.getID();
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
    public void added(String id) {

        addExecution(
                executionsApi.getExecution(id)
        );
    }

    @Override
    public void update(String id) {
        replace(
                executionsApi.getExecution(id)
        );
    }

    private void replace(Execution execution) {

        Integer row = idToRow.get(execution.getID());
        if (row == null)
            return;

        rowToExecution.put(row, execution);
        idToRow.put(execution.getID(), row);

        fireTableRowsUpdated(row, row);
    }
}
