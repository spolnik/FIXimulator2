package com.wordpress.nprogramming.oms.client;

import com.wordpress.nprogramming.oms.api.Execution;
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

public class ExecutionHttpClient implements Repository<Execution> {

    private final Client client;

    private static final Logger LOG = LoggerFactory.getLogger(ExecutionHttpClient.class);

    public ExecutionHttpClient() {
        client = ClientBuilder.newClient();
    }

    @Override
    public void save(Execution execution) {
        WebTarget target = client.target("http://localhost:8002")
                .path("api/executions");

        Response response = target.request()
                .post(Entity.json(execution));

        LOG.info("Response: " + response.toString());
        LOG.info("Response entity: " + response.getEntity().toString());
    }

    @Override
    public Execution queryById(String id) {

        WebTarget target = client.target("http://localhost:8002")
                .path("api/executions/" + id);

        return target.request(MediaType.APPLICATION_JSON)
                .get(Execution.class);
    }

    @Override
    public List<Execution> getAll() {
        WebTarget target = client.target("http://localhost:8002")
                .path("api/executions");

        return target.request(MediaType.APPLICATION_JSON).get(
                new GenericType<List<Execution>>(){
                }
        );
    }
}
