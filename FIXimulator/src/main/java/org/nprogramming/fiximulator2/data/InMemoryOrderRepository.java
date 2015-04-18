package org.nprogramming.fiximulator2.data;

import org.nprogramming.fiximulator2.api.OrderRepositoryWithCallback;
import org.nprogramming.fiximulator2.domain.Order;

import java.util.ArrayList;
import java.util.List;

public final class InMemoryOrderRepository
        extends InMemoryRepository<Order>
        implements OrderRepositoryWithCallback {

    private final List<Order> ordersToFill = new ArrayList<>();

    @Override
    public void addOrderToFill(Order orderToFill) {
        items.put(orderToFill.id(), orderToFill);

        ordersToFill.add(orderToFill);

        callback.update(orderToFill.id());
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
