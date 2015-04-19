package org.nprogramming.fiximulator2.ui.tables;

import com.wordpress.nprogramming.instruments.api.Instrument;
import com.wordpress.nprogramming.instruments.api.InstrumentsApi;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.Map;

public class InstrumentTableModel extends AbstractTableModel {

    private static final int TICKER = 0;
    private static final int NAME = 1;
    private static final int SEDOL = 2;
    private static final int RIC = 3;
    private static final int CUSIP = 4;
    private static final int PRICE = 5;

    private static final String[] headers =
            {"Ticker", "Name", "Sedol", "RIC", "Cusip", "Price"};

    private final transient Map<Integer, Instrument> rowToInstrument;

    public InstrumentTableModel(InstrumentsApi instrumentsApi) {

        rowToInstrument = new HashMap<>();

        instrumentsApi.getAll().forEach(
                this::addAndRefresh
        );
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

        if (column == PRICE) {
            return Double.class;
        }

        return String.class;
    }

    @Override
    public int getRowCount() {
        return rowToInstrument.size();
    }

    @Override
    public Object getValueAt(int row, int column) {

        Instrument instrument = get(row);

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

    private Instrument get(int row) {
        return rowToInstrument.get(row);
    }

    private void addAndRefresh(Instrument instrument) {
        int row = rowToInstrument.size();

        rowToInstrument.put(row, instrument);

        fireTableRowsInserted(row, row);
    }
}
