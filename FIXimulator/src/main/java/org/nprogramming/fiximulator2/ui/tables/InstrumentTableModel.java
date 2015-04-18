package org.nprogramming.fiximulator2.ui.tables;

import org.nprogramming.fiximulator2.api.InstrumentsApi;
import org.nprogramming.fiximulator2.domain.Instrument;

import javax.swing.table.AbstractTableModel;

public class InstrumentTableModel extends AbstractTableModel {

    private static final int TICKER = 0;
    private static final int NAME = 1;
    private static final int SEDOL = 2;
    private static final int RIC = 3;
    private static final int CUSIP = 4;
    private static final int PRICE = 5;

    private static final String[] headers =
        {"Ticker", "Name", "Sedol", "RIC", "Cusip", "Price"};

    private final transient InstrumentsApi instrumentsApi;

    public InstrumentTableModel(InstrumentsApi instrumentsApi){
        this.instrumentsApi = instrumentsApi;
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

        if (column == PRICE) {
            return Double.class;
        }

        return String.class;
    }

    @Override
    public int getRowCount() {
        return instrumentsApi.size();
    }

    @Override
    public Object getValueAt(int row, int column) {

        Instrument instrument = instrumentsApi.getInstrument(row);

        switch (column) {
            case TICKER:
                return instrument.getTicker();
            case NAME:
                return instrument.getName();
            case SEDOL:
                return instrument.getSedol();
            case RIC:
                return instrument.getRIC();
            case CUSIP:
                return instrument.getCusip();
            case PRICE:
                return Double.valueOf(instrument.getPrice());
            default:
                return new Object();
        }
    }
}
