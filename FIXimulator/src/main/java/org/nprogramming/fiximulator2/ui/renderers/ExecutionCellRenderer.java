package org.nprogramming.fiximulator2.ui.renderers;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ExecutionCellRenderer extends DefaultTableCellRenderer {

    private static final int DKD = 12;

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column
    ) {

        int myRow = table.convertRowIndexToModel(row);

        Component component = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, myRow, column);

        Boolean dontKnowTrade = (Boolean) table.getModel()
                .getValueAt(myRow, DKD);

        if (dontKnowTrade) {
            component.setForeground(Color.RED);
        } else {
            component.setForeground(Color.BLACK);
        }

        return component;
    }
}
