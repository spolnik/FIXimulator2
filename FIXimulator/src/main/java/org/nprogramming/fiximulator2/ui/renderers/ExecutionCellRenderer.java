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

        Component component = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        Boolean dontKnowTrade = (Boolean) table.getModel()
                .getValueAt(row, DKD);

        if (dontKnowTrade) {
            component.setForeground(Color.RED);
        } else {
            component.setForeground(Color.BLACK);
        }

        return component;
    }
}
