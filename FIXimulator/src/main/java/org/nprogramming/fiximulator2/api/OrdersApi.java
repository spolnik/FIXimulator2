package org.nprogramming.fiximulator2.api;

import org.nprogramming.fiximulator2.domain.Order;

public interface OrdersApi {

    void addOrderToFill(Order orderToFill);

    void addOrder(Order order);

    void update();

    boolean haveOrdersToFill();

    Order getOrderToFill();

    Order getOrder(int id);

    Order getOrder(String origClientID);

    void addCallback(Callback callback);

    int size();
}

