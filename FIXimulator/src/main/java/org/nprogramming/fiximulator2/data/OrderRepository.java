package org.nprogramming.fiximulator2.data;

import org.nprogramming.fiximulator2.api.NotifyApi;
import org.nprogramming.fiximulator2.api.OrdersApi;
import org.nprogramming.fiximulator2.core.FIXimulator;
import org.nprogramming.fiximulator2.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public final class OrderRepository implements OrdersApi {

    private final List<Order> orders = new ArrayList<>();
    private final List<Order> ordersToFill = new ArrayList<>();
    private NotifyApi notifyApi = null;

    private static final Logger LOG = LoggerFactory.getLogger(OrderRepository.class);

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
        if (toFill) ordersToFill.add(order);
        int limit = 50;
        try {
            limit = (int) FIXimulator.getApplication().getSettings()
                    .getLong("FIXimulatorCachedObjects");
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
        while (orders.size() > limit) {
            orders.remove(0);
        }
        notifyApi.update();
    }

    @Override
    public void update() {
        notifyApi.update();
    }

    @Override
    public void addCallback(NotifyApi notifyApi) {
        this.notifyApi = notifyApi;
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
            if (order.getID().equals(id) || order.getClientID().equals(id))
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
