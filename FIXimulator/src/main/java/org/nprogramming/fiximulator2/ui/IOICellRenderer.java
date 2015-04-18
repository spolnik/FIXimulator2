package org.nprogramming.fiximulator2.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class IOICellRenderer  extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        int myRow = table.convertRowIndexToModel(row);
        Component component = super.getTableCellRendererComponent(table, value,
                                          isSelected, hasFocus, myRow, column);
        String type = (String) table.getModel()
                .getValueAt(myRow, 1);
        if ("NEW".equals(type)) {
            component.setForeground(Color.BLACK);
        }
        if ("CANCEL".equals(type)) {
            component.setForeground(Color.RED);
        }
        if ("REPLACE".equals(type)) {
            component.setForeground(Color.BLUE);
        }
        return component;
    }        
}
