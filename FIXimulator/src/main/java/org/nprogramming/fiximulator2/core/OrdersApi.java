package org.nprogramming.fiximulator2.core;

import org.nprogramming.fiximulator2.ui.OrderTableModel;

public final class OrdersApi {
    private final OrderSet orders = new OrderSet();

    public void addOrderToFill(Order orderToFill) {
        orders.add(orderToFill, true);
    }

    public void addOrder(Order order) {
        orders.add(order, false);
    }

    public void update() {
        orders.update();
    }

    public boolean haveOrdersToFill() {
        return orders.haveOrdersToFill();
    }

    public Order getOrderToFill() {
        return orders.getOrderToFill();
    }

    public Order getOrder(int id) {
        return orders.getOrder(id);
    }

    public Order getOrder(String origClientID) {
        return orders.getOrder(origClientID);
    }

    public void addCallback(OrderTableModel orderTableModel) {
        orders.addCallback(orderTableModel);
    }

    public int getCount() {
        return orders.getCount();
    }
}

