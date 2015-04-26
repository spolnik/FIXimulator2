package com.wordpress.nprogramming.oms.client;

import com.wordpress.nprogramming.oms.api.Order;
import com.wordpress.nprogramming.oms.api.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class OrderHttpClient implements Repository<Order> {

    private  final Client client;

    private static final Logger LOG = LoggerFactory.getLogger(OrderHttpClient.class);

    public OrderHttpClient() {
        client = ClientBuilder.newClient();
    }

    @Override
    public void save(Order order) {
        WebTarget target = client.target("http://localhost:8002")
                .path("api/orders");

        Response response = target.request()
                .post(Entity.json(order));

        LOG.info("Response: " + response.toString());
        LOG.info("Response entity: " + response.getEntity().toString());
    }

    @Override
    public Order queryById(String id) {
        WebTarget target = client.target("http://localhost:8002")
                .path("api/orders/" + id);

        return target.request(MediaType.APPLICATION_JSON)
                .get(Order.class);
    }

    @Override
    public List<Order> getAll() {
        WebTarget target = client.target("http://localhost:8002")
                .path("api/orders");

        return target.request(MediaType.APPLICATION_JSON).get(
                new GenericType<List<Order>>(){
                }
        );
    }
}
