package com.wordpress.nprogramming.oms.service;

import com.wordpress.nprogramming.oms.api.Execution;
import com.wordpress.nprogramming.oms.api.Repository;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

@Path("/api/executions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExecutionResource {

    private final Repository<Execution> executionsRepository;

    public ExecutionResource(Repository<Execution> executionsRepository) {
        this.executionsRepository = executionsRepository;
    }

    @GET
    public List<Execution> listExecutions() {
        return executionsRepository.getAll();
    }

    @GET
    @Path("{executionId}")
    public Execution execution(@PathParam("executionId") String executionId) {
        return executionsRepository.queryById(executionId);
    }

    @POST
    public Response add(@Valid Execution execution) {
        executionsRepository.save(execution);

        return Response.created(UriBuilder.fromResource(ExecutionResource.class)
                        .build(execution.id())
        ).build();
    }
}
