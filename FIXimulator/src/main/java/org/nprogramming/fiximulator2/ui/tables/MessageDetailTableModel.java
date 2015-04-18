package org.nprogramming.fiximulator2.ui.tables;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import org.nprogramming.fiximulator2.fix.FIXimulator;
import org.nprogramming.fiximulator2.log4fix.LogMessage;
import org.nprogramming.fiximulator2.log4fix.LogMessageSet;
import org.nprogramming.fiximulator2.util.LogField;

public class MessageDetailTableModel extends AbstractTableModel 
        implements ListSelectionListener {

    private static LogMessageSet messages = FIXimulator.getMessageSet();

    private static final int FIELD = 0;
    private static final int TAG = 1;
    private static final int VALUE = 2;
    private static final int VALUE_NAME = 3;
    private static final int REQUIRED = 4;
    private static final int SECTION = 5;

    private JTable messageTable = null; 
    private final List<LogField> fields = new ArrayList<>();

    private static final String[] headers =
        {"Field", "Tag", "Value", "Value Name", "Required", "Section"};
        
    public MessageDetailTableModel(JTable messageTable){
        this.messageTable = messageTable;
        messageTable.getSelectionModel().addListSelectionListener(this);
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
        if (column == 1) return Integer.class;
        return String.class;
    }

    @Override
    public int getRowCount() {
        return fields.size();
    }

    @Override
    public Object getValueAt( int row, int column ) {
        LogField logField = fields.get( row );

        switch (column) {
            case FIELD:
                return logField.getFieldName();
            case TAG:
                return logField.getTag();
            case VALUE:
                return logField.getValue();
            case VALUE_NAME:
                return logField.getFieldValueName();
            case REQUIRED:
                return (logField.isRequired() ? "Yes" : "No");
            case SECTION:
                if (logField.isHeaderField())
                    return "Header";
                if (logField.isTrailerField())
                    return "Trailer";
                return "Body";
            default:
                return null;
        }
    }

    public void updateMessageDetailsTable(LogMessage message) {

        List<LogField> logFields = message.getLogFields();
        fields.clear();

        fields.addAll(
                logFields.stream()
                        .collect(Collectors.toList()));
        
        fireTableDataChanged();
    }

    @Override
    public void valueChanged(ListSelectionEvent selection) {
        if (!selection.getValueIsAdjusting()) {
            int row = messageTable.getSelectedRow();
            // if the first row is selected when it gets purged
            if ( row != -1 ) {
                row = messageTable.convertRowIndexToModel(row);
                LogMessage msg = messages.getMessage( row );
                updateMessageDetailsTable(msg);
            }
        }
    }
}
