package org.nprogramming.fiximulator2.data;

import com.wordpress.nprogramming.oms.api.Order;
import com.wordpress.nprogramming.oms.api.Repository;

public interface OrdersRepository extends Repository<Order> {

    void addOrderToFill(Order orderToFill);

    boolean haveOrdersToFill();

    Order getOrderToFill();
}
