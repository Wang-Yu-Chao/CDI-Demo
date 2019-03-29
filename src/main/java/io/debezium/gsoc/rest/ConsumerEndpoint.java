package io.debezium.gsoc.rest;

import io.debezium.gsoc.service.ConsumerService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/consumer")
@Produces(MediaType.APPLICATION_JSON)
public class ConsumerEndpoint {

    @Inject
    ConsumerService service;

    @GET
    public Response getRecords() {
        return Response.ok(service.getRecords()).build();
    }
}
