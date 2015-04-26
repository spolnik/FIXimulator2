package org.nprogramming.fiximulator2.ui.tables;

import org.nprogramming.fiximulator2.api.MessageHandler;
import org.nprogramming.fiximulator2.api.NotifyService;
import org.nprogramming.fiximulator2.api.OrderRepository;
import com.wordpress.nprogramming.oms.api.Order;
import org.nprogramming.fiximulator2.api.event.OrderChanged;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.Map;

import static org.nprogramming.fiximulator2.domain.OrderFieldMapper.expandSide;

public class OrderTableModel extends AbstractTableModel implements MessageHandler<OrderChanged> {

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

    private final transient Map<Integer, Order> rowToOrder;
    private final transient Map<String, Integer> idToRow;

    private final transient OrderRepository orderRepository;

    public OrderTableModel(OrderRepository orderRepository, NotifyService notifyService) {
        this.orderRepository = orderRepository;

        rowToOrder = new HashMap<>();
        idToRow = new HashMap<>();

        orderRepository.getAll().forEach(
                this::addOrReplaceAndRefresh
        );

        notifyService.register(OrderChanged.class, this);
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
        return rowToOrder.size();
    }

    @Override
    public Object getValueAt(int row, int column) {

        Order order = get(row);

        switch (column) {
            case ID:
                return order.id();
            case STATUS:
                return order.getStatus();
            case SIDE:
                return expandSide(order.getFIXSide());
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
    public void onMessage(OrderChanged message) {
        addOrReplaceAndRefresh(
                orderRepository.get(message.id())
        );
    }

    public Order get(int row) {
        return rowToOrder.get(row);
    }

    private void addAndRefresh(Order order) {
        int row = rowToOrder.size();

        rowToOrder.put(row, order);
        idToRow.put(order.id(), row);

        fireTableRowsInserted(row, row);
    }

    private void addOrReplaceAndRefresh(Order order) {

        Integer row = idToRow.get(order.id());

        if (row == null) {
            addAndRefresh(order);
            return;
        }

        rowToOrder.put(row, order);
        idToRow.put(order.id(), row);

        fireTableRowsUpdated(row, row);
    }
}
