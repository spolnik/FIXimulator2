package org.nprogramming.fiximulator2.api;

import com.wordpress.nprogramming.oms.api.Order;

public interface OrderRepository extends Repository<Order> {

    void addOrderToFill(Order orderToFill);

    boolean haveOrdersToFill();

    Order getOrderToFill();
}
