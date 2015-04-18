package org.nprogramming.fiximulator2.ui.tables;

import org.nprogramming.fiximulator2.api.NotifyApi;
import org.nprogramming.fiximulator2.api.RepositoryWithCallback;
import org.nprogramming.fiximulator2.domain.IOI;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.Map;

public class IOITableModel extends AbstractTableModel implements NotifyApi {

    private static final int ID = 0;
    private static final int TYPE = 1;
    private static final int SIDE = 2;
    private static final int SHARES = 3;
    private static final int SYMBOL = 4;
    private static final int PRICE = 5;
    private static final int SECURITY_ID = 6;
    private static final int ID_SOURCE = 7;
    private static final int NATURAL = 8;
    private static final int REF_ID = 9;

    private static final String[] headers =
        {"ID", "Type", "Side", "Shares", "Symbol", "Price", 
         "SecurityID", "IDSource", "Natural", "RefID"};

    private final transient Map<Integer, IOI> rowToIOI;
    private final transient Map<String, Integer> idToRow;

    private final transient RepositoryWithCallback<IOI> ioiRepository;


    public IOITableModel(RepositoryWithCallback<IOI> ioiRepository){
        this.ioiRepository = ioiRepository;

        rowToIOI = new HashMap<>();
        idToRow = new HashMap<>();

        ioiRepository.getAll().forEach(
                this::addAndRefresh
        );

        ioiRepository.addCallback(this);
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
            case SHARES:
            case PRICE:
                return Double.class;
            default:
                return String.class;
        }
    }

    @Override
    public int getRowCount() {
        return rowToIOI.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        IOI ioi = get(row);

        switch (column) {
            case ID:
                return ioi.id();
            case TYPE:
                return ioi.getType();
            case SIDE:
                return ioi.getSide();
            case SHARES:
                return ioi.getQuantity();
            case SYMBOL:
                return ioi.getSymbol();
            case PRICE:
                return ioi.getPrice();
            case SECURITY_ID:
                return ioi.getSecurityID();
            case ID_SOURCE:
                return ioi.getIDSource();
            case NATURAL:
                return ioi.getNatural();
            case REF_ID:
                return ioi.getRefID();
            default:
                return "";
        }
    }

    @Override
    public void added(String id) {

        addAndRefresh(
                ioiRepository.get(id)
        );
    }

    @Override
    public void update(String id) {
        replaceAndRefresh(
                ioiRepository.get(id)
        );
    }

    public IOI get(int row) {
        return rowToIOI.get(row);
    }

    private void addAndRefresh(IOI ioi) {
        int row = rowToIOI.size();

        rowToIOI.put(row, ioi);
        idToRow.put(ioi.id(), row);

        fireTableRowsInserted(row, row);
    }

    private void replaceAndRefresh(IOI ioi) {

        Integer row = idToRow.get(ioi.id());
        if (row == null)
            return;

        rowToIOI.put(row, ioi);
        idToRow.put(ioi.id(), row);

        fireTableRowsUpdated(row, row);
    }
}
