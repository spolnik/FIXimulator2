package com.wordpress.nprogramming.oms.service;

import com.wordpress.nprogramming.oms.api.Order;
import com.wordpress.nprogramming.oms.api.Repository;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

@Path("/api/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    private final Repository<Order> ordersRepository;

    public OrderResource(Repository<Order> ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @GET
    public List<Order> listOrders() {
        return ordersRepository.getAll();
    }

    @GET
    @Path("{orderId}")
    public Order order(@PathParam("orderId") String orderId) {
        return ordersRepository.queryById(orderId);
    }

    @POST
    public Response add(@Valid Order order) {
        ordersRepository.save(order);

        return Response.created(UriBuilder.fromResource(OrderResource.class)
                        .build(order.id())
        ).build();
    }
}
