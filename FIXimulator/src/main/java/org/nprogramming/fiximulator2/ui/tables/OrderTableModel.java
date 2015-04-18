package org.nprogramming.fiximulator2.ui.tables;

import org.nprogramming.fiximulator2.api.Callback;
import org.nprogramming.fiximulator2.domain.Order;
import org.nprogramming.fiximulator2.api.OrdersApi;

import javax.swing.table.AbstractTableModel;

public class OrderTableModel extends AbstractTableModel implements Callback {

    private static final int ID = 0;
    private static final int STATUS = 1;
    private static final int SIDE = 2;
    private static final int QUANTITY = 3;
    private static final int SYMBOL = 4;
    private static final int TYPE = 5;
    private static final int LIMIT = 6;
    private static final int TIF = 7;
    private static final int EXECUTED = 8;
    private static final int OPEN = 9;
    private static final int AVG_PX = 10;
    private static final int CL_ORD_ID = 11;
    private static final int ORIG_CL_ORD_ID = 12;

    private static String[] headers =
            {"ID", "Status", "Side", "Quantity", "Symbol", "Type", "Limit", "TIF",
                    "Executed", "Open", "AvgPx", "ClOrdID", "OrigClOrdID"};

    private final OrdersApi ordersApi;

    public OrderTableModel(OrdersApi ordersApi) {
        this.ordersApi = ordersApi;
        ordersApi.addCallback(this);
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
            case QUANTITY:
            case LIMIT:
            case EXECUTED:
            case OPEN:
            case AVG_PX:
                return Double.class;
            default:
                return String.class;
        }
    }

    @Override
    public int getRowCount() {
        return ordersApi.size();
    }

    @Override
    public Object getValueAt(int row, int column) {

        Order order = ordersApi.getOrder(row);

        switch (column) {
            case ID:
                return order.getID();
            case STATUS:
                return order.getStatus();
            case SIDE:
                return order.getSide();
            case QUANTITY:
                return order.getQuantity();
            case SYMBOL:
                return order.getSymbol();
            case TYPE:
                return order.getOrderType();
            case LIMIT:
                return order.getPriceLimit();
            case TIF:
                return order.getTimeInForce();
            case EXECUTED:
                return order.getExecuted();
            case OPEN:
                return order.getOpen();
            case AVG_PX:
                return order.getAvgPx();
            case CL_ORD_ID:
                return order.getClientOrderID();
            case ORIG_CL_ORD_ID:
                return order.getOrigClientID();
            default:
                return new Object();
        }
    }

    @Override
    public void update() {
        fireTableDataChanged();
    }
}
