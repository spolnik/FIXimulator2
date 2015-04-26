package org.nprogramming.fiximulator2.data;

import org.nprogramming.fiximulator2.api.OrderRepository;
import com.wordpress.nprogramming.oms.api.Order;

import java.util.ArrayList;
import java.util.List;

public final class InMemoryOrderRepository
        extends InMemoryRepository<Order>
        implements OrderRepository {

    private final List<Order> ordersToFill = new ArrayList<>();

    @Override
    public void addOrderToFill(Order orderToFill) {
        items.put(orderToFill.id(), orderToFill);

        ordersToFill.add(orderToFill);
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
