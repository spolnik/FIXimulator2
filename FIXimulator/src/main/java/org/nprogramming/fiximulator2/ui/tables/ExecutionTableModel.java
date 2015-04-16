package org.nprogramming.fiximulator2.ui.tables;

import javax.swing.table.AbstractTableModel;

import org.nprogramming.fiximulator2.api.ExecutionsApi;
import org.nprogramming.fiximulator2.api.Callback;
import org.nprogramming.fiximulator2.domain.Execution;
import org.nprogramming.fiximulator2.domain.Order;

public class ExecutionTableModel extends AbstractTableModel implements Callback {

    private static String[] columns = 
        {"ID", "ClOrdID", "Side", "Symbol", "LastQty", "LastPx", 
         "CumQty", "AvgPx", "Open", "ExecType", "ExecTranType", "RefID", "DKd"};
    private final ExecutionsApi executionsApi;


    public ExecutionTableModel(ExecutionsApi executionsApi){
        this.executionsApi = executionsApi;
        executionsApi.addCallback(this);
    }
    
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Class getColumnClass(int column) {
        if (column == 4) return Double.class;
        if (column == 5) return Double.class;
        if (column == 6) return Double.class;
        if (column == 7) return Double.class;
        if (column == 8) return Double.class;
        return String.class;
    }
        
    public int getRowCount() {
        return executionsApi.size();
    }

    public Object getValueAt(int row, int column) {
        Execution execution = executionsApi.getExecution(row);
        Order order = execution.getOrder();
        if (column == 0) return execution.getID();        
        if (column == 1) return order.getClientID();
        if (column == 2) return order.getSide();
        if (column == 3) return order.getSymbol();
        if (column == 4) return execution.getLastShares();
        if (column == 5) return execution.getLastPx();
        if (column == 6) return execution.getCumQty();
        if (column == 7) return execution.getAvgPx();
        if (column == 8) return order.getOpen();
        if (column == 9) return execution.getExecType();
        if (column == 10) return execution.getExecTranType();
        if (column == 11) return execution.getRefID();
        if (column == 12) return execution.isDKd();
        return "";
    }

    @Override
    public void update() {
        fireTableDataChanged();
    }
}
