package com.wordpress.nprogramming.oms.client;

import com.wordpress.nprogramming.oms.api.IOI;
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

public class IOIHttpClient implements Repository<IOI> {

    private final Client client;

    private static final Logger LOG = LoggerFactory.getLogger(IOIHttpClient.class);

    public IOIHttpClient() {
        client = ClientBuilder.newClient();
    }

    @Override
    public void save(IOI ioi) {
        WebTarget target = client.target("http://localhost:8002")
                .path("api/ioi");

        Response response = target.request()
                .post(Entity.json(ioi));

        LOG.info("Response: " + response.toString());
        LOG.info("Response entity: " + response.getEntity().toString());
    }

    @Override
    public IOI queryById(String id) {

        WebTarget target = client.target("http://localhost:8002")
                .path("api/ioi/" + id);

        return target.request(MediaType.APPLICATION_JSON)
                .get(IOI.class);
    }

    @Override
    public List<IOI> getAll() {
        WebTarget target = client.target("http://localhost:8002")
                .path("api/ioi");

        return target.request(MediaType.APPLICATION_JSON).get(
                new GenericType<List<IOI>>(){
                }
        );
    }

    public static void main(String[] args) {
        IOIHttpClient httpClient = new IOIHttpClient();

        String id = "IBM";
        IOI ioiToSave = new IOI(id);
        ioiToSave.setIDSource("TICKER");
        ioiToSave.setPrice(2.22);
        httpClient.save(ioiToSave);

        IOI ioi = httpClient.queryById(id);

        LOG.info("ioi('" + id + "')");
        LOG.info(ioi.toString());
    }
}
