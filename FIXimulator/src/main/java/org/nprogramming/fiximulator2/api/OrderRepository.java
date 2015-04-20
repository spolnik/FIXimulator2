package org.nprogramming.fiximulator2.api;

import org.nprogramming.fiximulator2.domain.Order;

public interface OrderRepository extends Repository<Order> {

    void addOrderToFill(Order orderToFill);

    boolean haveOrdersToFill();

    Order getOrderToFill();
}
