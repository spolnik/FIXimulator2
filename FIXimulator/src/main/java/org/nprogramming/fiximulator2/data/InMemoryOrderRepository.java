package org.nprogramming.fiximulator2.data;

import com.wordpress.nprogramming.oms.api.Order;
import com.wordpress.nprogramming.oms.api.Repository;

import java.util.ArrayList;
import java.util.List;

public final class InMemoryOrderRepository implements OrdersRepository {

    private final List<Order> ordersToFill = new ArrayList<>();
    private final Repository<Order> ordersRepository;

    public InMemoryOrderRepository(Repository<Order> ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @Override
    public void addOrderToFill(Order orderToFill) {
        ordersRepository.save(orderToFill);
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

    @Override
    public void save(Order order) {
        ordersRepository.save(order);
    }

    @Override
    public Order queryById(String id) {
        return ordersRepository.queryById(id);
    }

    @Override
    public List<Order> getAll() {
        return ordersRepository.getAll();
    }
}
