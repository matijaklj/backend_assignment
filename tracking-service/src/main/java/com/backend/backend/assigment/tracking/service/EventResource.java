package com.backend.backend.assigment.tracking.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * @author Matija Kljun
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@Path("")
public class EventResource {

    private static final Logger log = Logger.getLogger(EventResource.class.getName());

    @Inject
    private EventService eventService;

    @POST
    @Path("{accountId}")
    public Response publishEvent(@PathParam("accountId") String accountId,
                                                     @QueryParam("data") String data) {
        log.info(String.format("Received event from accountId: %s with data: %s", accountId, data));

        CompletableFuture<Response> future = new CompletableFuture<>();

        this.eventService.publishEvent(accountId, data, future);

        try {
            return future.get();
        } catch (Exception e) {
            return Response.serverError().build();
        }

    }

}
