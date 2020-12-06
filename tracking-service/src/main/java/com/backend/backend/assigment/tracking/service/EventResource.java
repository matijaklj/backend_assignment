package com.backend.backend.assigment.tracking.service;

import com.backend.backend.assigment.tracking.service.AccountService;
import com.backend.backend.assigment.tracking.service.entities.Account;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.streaming.common.annotations.StreamProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;

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
@Path("events")
public class EventResource {

    private static final Logger log = Logger.getLogger(EventResource.class.getName());

    @Inject
    private EventService eventService;

    @POST
    @Path("{accountId}")
    public CompletableFuture<Response> publishEvent(@PathParam("accountId") String accountId,
                                                     @QueryParam("data") String data) {
        log.info(String.format("Received event from accountId: %s with data: %s", accountId, data));

        CompletableFuture<Response> future = new CompletableFuture<>();

        this.eventService.publishEvent(accountId, data, future);

        return future;

    }

    /*
    @POST
    @Path("{accountId}")
    public CompletableFuture<String> publishEventOLD(@PathParam("accountId") String accountId,
                                 @QueryParam("data") String data) {
        log.info(String.format("Received event from accountId: %s with data: %s", accountId, data));

        CompletableFuture<String> future = new CompletableFuture<>();

        // check if accountId is in the database
        Account account = this.accountService.getAccount(accountId);
        if (account == null) {
            log.info(String.format("Account with accountId: %s not found.", accountId));
            //return Response.status(Response.Status.NOT_FOUND).build();
            future.complete("404");
            return future;
        }

        if (!account.isActive()) {
            log.info(String.format("Account with accountId: %s not active.", accountId));
            //return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            future.complete("404");
            return future;
        }

        // get topic from configa
        //String topicName = ConfigurationUtil.getInstance().get("kafka.topic.name").orElse("event-data");
        String key = accountId;

        ProducerRecord<String, String> record = new ProducerRecord<String, String>("event-"+accountId, key, data);

        // todo can we get a response if it failed?
        try {
            //kafkaProducer.beginTransaction();
            kafkaProducer.send(record,
                    (metadata, e) -> {
                        if (e != null) {
                            log.warning("Error message not sent. " + e.getMessage());
                            future.complete("Error with kafka");
                        } else {
                            log.info("The offset of the produced event record is: " + metadata.offset());
                            future.complete(Long.toString(metadata.offset()));
                        }
                    });
        } catch (KafkaException e) {
            // For all other exceptions, just abort the transaction and try again.
            //kafkaProducer.abortTransaction();
            future.complete("EROOOOOOR");
        }
        //return Response.ok().build();
        return future;

    }
     */
}
