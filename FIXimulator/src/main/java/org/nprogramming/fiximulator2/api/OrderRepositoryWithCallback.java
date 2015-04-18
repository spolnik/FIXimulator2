package org.nprogramming.fiximulator2.api;

import org.nprogramming.fiximulator2.domain.Order;

public interface OrderRepositoryWithCallback extends RepositoryWithCallback<Order> {

    void addOrderToFill(Order orderToFill);

    boolean haveOrdersToFill();

    Order getOrderToFill();
}
