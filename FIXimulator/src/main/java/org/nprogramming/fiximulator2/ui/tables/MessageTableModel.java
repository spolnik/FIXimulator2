package org.nprogramming.fiximulator2.ui.tables;

import javax.swing.table.AbstractTableModel;

import org.nprogramming.fiximulator2.api.Callback;
import org.nprogramming.fiximulator2.fix.FIXimulator;
import org.nprogramming.fiximulator2.log4fix.LogMessage;
import org.nprogramming.fiximulator2.log4fix.LogMessageSet;
import quickfix.field.converter.UtcTimestampConverter;

public class MessageTableModel extends AbstractTableModel implements Callback {

    private static LogMessageSet messages = FIXimulator.getMessageSet();

    private static final int ID = 0;
    private static final int DIRECTION = 1;
    private static final int SENDING_TIME = 2;
    private static final int TYPE = 3;
    private static final int MESSAGE = 4;

    private static final String[] headers =
        {"#", "Direction", "SendingTime", "Type", "Message"};
    
    public MessageTableModel(){
        messages.addCallback(this);
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
        if (column == 0) return Integer.class;
        return String.class;
    }

    @Override
    public int getRowCount() {
        return messages.size();
    }

    @Override
    public Object getValueAt( int row, int column ) {

        LogMessage msg = messages.getMessage( row );

        switch (column) {
            case ID:
                return msg.getMessageIndex();
            case DIRECTION:
                return (msg.isIncoming() ? "incoming" : "outgoing");
            case SENDING_TIME:
                return UtcTimestampConverter.convert(msg.getSendingTime(), true);
            case TYPE:
                return msg.getMessageTypeName();
            case MESSAGE:
                return msg.getRawMessage();
            default:
                return new Object();
        }
    }

    @Override
    public void update() {
        fireTableDataChanged();
    }
}
