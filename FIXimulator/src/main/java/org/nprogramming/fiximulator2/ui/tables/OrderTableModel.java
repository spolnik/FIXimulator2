package org.nprogramming.fiximulator2.ui.tables;

import org.nprogramming.fiximulator2.api.Callback;
import org.nprogramming.fiximulator2.domain.Order;
import org.nprogramming.fiximulator2.api.OrdersApi;

import javax.swing.table.AbstractTableModel;

public class OrderTableModel extends AbstractTableModel implements Callback {

    private static String[] columns =
        {"ID", "Status", "Side", "Quantity", "Symbol", "Type", "Limit", "TIF", 
         "Executed", "Open", "AvgPx", "ClOrdID", "OrigClOrdID"};
    private final OrdersApi ordersApi;


    public OrderTableModel(OrdersApi ordersApi){
        this.ordersApi = ordersApi;
        ordersApi.addCallback(this);
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
        if (column == 3) return Double.class;
        if (column == 6) return Double.class;
        if (column == 8) return Double.class;
        if (column == 9) return Double.class;
        if (column == 10) return Double.class;
        return String.class;
    }
        
    public int getRowCount() {
        return ordersApi.size();
    }

    public Object getValueAt(int row, int column) {
        Order order = ordersApi.getOrder(row);
        if (column == 0) return order.getID();        
        if (column == 1) return order.getStatus();
        if (column == 2) return order.getSide();
        if (column == 3) return order.getQuantity();
        if (column == 4) return order.getSymbol();
        if (column == 5) return order.getOrderType();
        if (column == 6) return order.getPriceLimit();
        if (column == 7) return order.getTimeInForce();
        if (column == 8) return order.getExecuted();
        if (column == 9) return order.getOpen();
        if (column == 10) return order.getAvgPx();
        if (column == 11) return order.getClientOrderID();
        if (column == 12) return order.getOrigClientID();
        return new Object();
    }

    @Override
    public void update() {
        fireTableDataChanged();
    }
}