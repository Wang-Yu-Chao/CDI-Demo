package io.debezium.gsoc.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.gsoc.service.ConnectorService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestScoped
@Path("/connector")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConnectorEndpoint {

    @Inject
    ConnectorService connectorService;

    @GET
    public Response simpleGet() {
        return Response.ok().build();
    }

    @GET
    @Path("config")
    public Response getEngineConfigs() {
        return Response.ok(connectorService.getEngineConfig()).build();
    }

    @POST
    @Path("config")
    public Response createEngineWithConfig(String jsonData) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonData);
            JsonNode nameNode = rootNode.path("name");
            JsonNode configNode = rootNode.path("config");

            map.put("name", nameNode.asText());
            configNode.fields().forEachRemaining((entry) -> { map.put(entry.getKey(), entry.getValue().asText()); });
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }

        connectorService.createEngine(map);

        return Response.ok().build();
    }
}
