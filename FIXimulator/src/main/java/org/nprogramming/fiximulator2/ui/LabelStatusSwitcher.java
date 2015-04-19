package org.nprogramming.fiximulator2.ui;

import org.nprogramming.fiximulator2.core.StatusSwitcher;

import javax.swing.*;

public final class LabelStatusSwitcher implements StatusSwitcher {

    private final JLabel label;

    private final ImageIcon green = new ImageIcon(getClass()
            .getResource("/img/green.gif"));

    private final ImageIcon red = new ImageIcon(getClass()
        .getResource("/img/red.gif"));

    public LabelStatusSwitcher(JLabel label) {

        this.label = label;
    }

    @Override
    public void on() {
        label.setIcon(green);
    }

    @Override
    public void off() {
        label.setIcon(red);
    }
}
