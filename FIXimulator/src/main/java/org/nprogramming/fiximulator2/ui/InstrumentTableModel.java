/*
 * File     : InstrumentTableModel.java
 *
 * Author   : Zoltan Feledy
 * 
 * Contents : This class is the TableModel for the Instrument Table.
 * 
 */

package org.nprogramming.fiximulator2.ui;

import javax.swing.table.AbstractTableModel;
import org.nprogramming.fiximulator2.core.FIXimulator;
import org.nprogramming.fiximulator2.domain.Instrument;
import org.nprogramming.fiximulator2.data.InstrumentSet;

public class InstrumentTableModel extends AbstractTableModel {
    private static InstrumentSet instruments = FIXimulator.getInstruments();
    private static String[] columns = 
        {"Ticker", "Name", "Sedol", "RIC", "Cusip", "Price"}; 

    public InstrumentTableModel(){
        FIXimulator.getInstruments().addCallback(this);
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
        return instruments.getCount();
    }

    public Object getValueAt(int row, int column) {
        Instrument instrument = instruments.getInstrument(row);
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
