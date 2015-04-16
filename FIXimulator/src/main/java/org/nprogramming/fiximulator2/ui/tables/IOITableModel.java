package org.nprogramming.fiximulator2.ui.tables;

import org.nprogramming.fiximulator2.api.Callback;
import org.nprogramming.fiximulator2.domain.IOI;
import org.nprogramming.fiximulator2.api.IndicationsOfInterestApi;

import javax.swing.table.AbstractTableModel;

public class IOITableModel extends AbstractTableModel implements Callback {

    private static String[] columns =
        {"ID", "Type", "Side", "Shares", "Symbol", "Price", 
         "SecurityID", "IDSource", "Natural", "RefID"};

    private final IndicationsOfInterestApi indicationsOfInterestApi;


    public IOITableModel(IndicationsOfInterestApi indicationsOfInterestApi){
        this.indicationsOfInterestApi = indicationsOfInterestApi;
        indicationsOfInterestApi.addCallback(this);
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
        if (column == 5) return Double.class;
        return String.class;
    }

    public int getRowCount() {
        return indicationsOfInterestApi.size();
    }

    public Object getValueAt(int row, int column) {
        IOI ioi = indicationsOfInterestApi.getIOI(row);
        if (column == 0) return ioi.getID();
        if (column == 1) return ioi.getType();
        if (column == 2) return ioi.getSide();
        if (column == 3) return ioi.getQuantity();
        if (column == 4) return ioi.getSymbol();
        if (column == 5) return ioi.getPrice();
        if (column == 6) return ioi.getSecurityID();
        if (column == 7) return ioi.getIDSource();
        if (column == 8) return ioi.getNatural();
        if (column == 9) return ioi.getRefID();
        return new Object();
    }

    @Override
    public void update() {
        fireTableDataChanged();
    }
}
