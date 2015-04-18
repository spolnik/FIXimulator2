package org.nprogramming.fiximulator2.data;

import org.nprogramming.fiximulator2.api.Callback;
import org.nprogramming.fiximulator2.api.OrdersApi;
import org.nprogramming.fiximulator2.domain.Order;

import java.util.ArrayList;
import java.util.List;

public final class OrderRepository implements OrdersApi {

    private final List<Order> orders = new ArrayList<>();
    private final List<Order> ordersToFill = new ArrayList<>();

    private Callback callback = null;

    @Override
    public void addOrderToFill(Order orderToFill) {
        add(orderToFill, true);
    }

    @Override
    public void addOrder(Order order) {
        add(order, false);
    }

    private void add(Order order, boolean toFill) {
        orders.add(order);
        if (toFill)
            ordersToFill.add(order);

        callback.update();
    }

    @Override
    public void update() {
        callback.update();
    }

    @Override
    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public int size() {
        return orders.size();
    }

    @Override
    public Order getOrder(int id) {
        return orders.get(id);
    }

    @Override
    public Order getOrder(String id) {
        for (Order order : orders) {
            if (order.getID().equals(id) || order.getClientOrderID().equals(id))
                return order;
        }
        return null;
    }

    @Override
    public boolean haveOrdersToFill() {
        return !ordersToFill.isEmpty();
    }

    @Override
    public Order getOrderToFill() {
        return ordersToFill.remove(0);
    }
}
