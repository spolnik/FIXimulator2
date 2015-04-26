package com.wordpress.nprogramming.oms.service;

import com.wordpress.nprogramming.oms.api.Execution;
import com.wordpress.nprogramming.oms.api.IOI;
import com.wordpress.nprogramming.oms.api.Order;
import com.wordpress.nprogramming.oms.api.Repository;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class OrderManagementSystemApplication extends Application<OrderManagementSystemConfiguration> {

    public static void main(String[] args) throws Exception {
        new OrderManagementSystemApplication().run(args);
    }

    @Override
    public String getName() {
        return "oms-service";
    }

    @Override
    public void run(
            OrderManagementSystemConfiguration configuration, Environment environment
    ) throws Exception {

        final Repository<IOI> ioiRepository = new InMemoryRepository<>();
        final Repository<Execution> executionRepository = new InMemoryRepository<>();
        final Repository<Order> ordersRepository = new InMemoryRepository<>();

        final IndicationOfInterestResource indicationOfInterestResource =
                new IndicationOfInterestResource(ioiRepository);

        final ExecutionResource executionResource =
                new ExecutionResource(executionRepository);

        final OrderResource orderResource =
                new OrderResource(ordersRepository);

        environment.jersey().register(indicationOfInterestResource);
        environment.jersey().register(executionResource);
        environment.jersey().register(orderResource);
    }
}
