package com.wordpress.nprogramming.oms.service;

import com.wordpress.nprogramming.oms.api.IOI;
import com.wordpress.nprogramming.oms.api.Repository;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

@Path("/api/ioi")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IndicationOfInterestResource {

    private final Repository<IOI> ioiRepository;

    public IndicationOfInterestResource(Repository<IOI> ioiRepository) {
        this.ioiRepository = ioiRepository;
    }

    @GET
    public List<IOI> listIOIs() {
        return ioiRepository.getAll();
    }

    @GET
    @Path("{ioiId}")
    public IOI ioi(@PathParam("ioiId") String ioiId) {
        return ioiRepository.queryById(ioiId);
    }

    @POST
    public Response add(@Valid IOI ioi) {
        ioiRepository.save(ioi);

        return Response.created(UriBuilder.fromResource(IndicationOfInterestResource.class)
                .build(ioi.id())
        ).build();
    }
}
