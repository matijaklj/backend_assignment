package com.backend.backend.assigment.tracking.service;

import com.backend.backend.assigment.tracking.service.entities.Account;
import com.kumuluz.ee.streaming.common.annotations.StreamProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@RequestScoped
public class EventService {

    private static final Logger log = Logger.getLogger(EventService.class.getName());

    @Inject
    @StreamProducer
    private Producer kafkaProducer;

    @Inject
    private AccountService accountService;

    public void publishEvent(String accountId, String data, CompletableFuture<Response> future) {
        Account account = this.accountService.getAccount(accountId);
        if (account == null) {
            log.info(String.format("Account with accountId: %s not found.", accountId));
            future.complete(Response.status(Response.Status.NOT_FOUND).build());
            return;
        }

        if (!account.isActive()) {
            log.info(String.format("Account with accountId: %s not active.", accountId));
            future.complete(Response.status(Response.Status.BAD_REQUEST).build());
            return;
        }

        ProducerRecord<String, String> record = new ProducerRecord<String, String>("event-"+accountId, accountId, data);

        kafkaProducer.send(record,
                (metadata, e) -> {
                    if (e != null) {
                        log.warning("Error kafka broker not reachable. " + e.getMessage());
                        future.complete(Response.status(Response.Status.SERVICE_UNAVAILABLE).build());
                    } else {
                        log.info("The offset of the produced event record is: " + metadata.offset());
                        future.complete(Response.ok().entity(metadata.offset()).build());
                    }
                });
    }
}
