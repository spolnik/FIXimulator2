package com.wordpress.nprogramming.oms.client;

import com.wordpress.nprogramming.oms.api.Order;

public class OrderHttpClient extends BaseHttpClient<Order> {

    public OrderHttpClient() {
        super(Order.class);
    }

    @Override
    protected String path() {
        return "api/orders";
    }
}
