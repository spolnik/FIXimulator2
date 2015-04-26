package com.wordpress.nprogramming.oms.client;

import com.wordpress.nprogramming.oms.api.ItemWithId;
import com.wordpress.nprogramming.oms.api.Repository;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

public abstract class BaseHttpClient<TMessage extends ItemWithId> implements Repository<TMessage> {

    private final Client client;

    private static final String TARGET = "http://localhost:8002";
    private final Class<TMessage> clazz;

    public BaseHttpClient(Class<TMessage> clazz) {
        this.clazz = clazz;
        client = ClientBuilder.newClient();
    }

    @Override
    public void save(TMessage message) {

        WebTarget target = client.target(TARGET)
                .path(path());

        target.request().post(Entity.json(message));
    }

    @Override
    public TMessage queryById(String id) {
        WebTarget target = client.target(TARGET)
                .path(path() + "/" + id);

        return target.request(MediaType.APPLICATION_JSON)
                .get(clazz);
    }

    @Override
    public List<TMessage> getAll() {
        WebTarget target = client.target(TARGET)
                .path(path());

        return target.request(MediaType.APPLICATION_JSON).get(
                new GenericType<List<TMessage>>(){
                }
        );
    }

    protected abstract String path();
}
