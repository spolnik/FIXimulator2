/*
 * File     : InstrumentTableModel.java
 *
 * Author   : Zoltan Feledy
 * 
 * Contents : This class is the TableModel for the Instrument Table.
 * 
 */

package org.nprogramming.fiximulator2.ui.tables;

import javax.swing.table.AbstractTableModel;

import org.nprogramming.fiximulator2.api.InstrumentsApi;
import org.nprogramming.fiximulator2.api.Callback;
import org.nprogramming.fiximulator2.domain.Instrument;

public class InstrumentTableModel extends AbstractTableModel implements Callback {
    private static String[] columns =
        {"Ticker", "Name", "Sedol", "RIC", "Cusip", "Price"};
    private final InstrumentsApi instrumentsApi;

    public InstrumentTableModel(InstrumentsApi instrumentsApi){
        this.instrumentsApi = instrumentsApi;
        instrumentsApi.addCallback(this);
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
        if (column == 5) return Double.class;
        return String.class;
    }
        
    public int getRowCount() {
        return instrumentsApi.size();
    }

    public Object getValueAt(int row, int column) {
        Instrument instrument = instrumentsApi.getInstrument(row);
        if (column == 0) return instrument.getTicker();
        if (column == 1) return instrument.getName();
        if (column == 2) return instrument.getSedol();
        if (column == 3) return instrument.getRIC();
        if (column == 4) return instrument.getCusip();
        if (column == 5) return Double.valueOf(instrument.getPrice());
        return new Object();
    }
    
    public void update() {
        fireTableDataChanged();
    }
}
