package com.backend.backend.assigment.tracking.service.tests;

import com.backend.backend.assigment.tracking.service.AccountService;
import com.backend.backend.assigment.tracking.service.EventService;
import com.backend.backend.assigment.tracking.service.entities.Account;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.net.ssl.SSLEngineResult;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EventServiceTest extends Arquillian {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClass(AccountService.class)
                .addClass(Account.class)
                .addClass(EventService.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("config.yml", "../config.yml")
                .addAsManifestResource("META-INF/data/accounts.sql", "data/accounts.sql");
    }

    @Inject
    private EventService eventService;

    @Test
    public void testAcountServiceInjectable() {
        Assert.assertNotNull(eventService);
    }

    @Test
    public void publishEvent() {
        CompletableFuture<Response> future = new CompletableFuture<>();
        eventService.publishEvent("0", "data", future);

        try {
            Response response = future.get();
            Assert.assertNotNull(response);
            Assert.assertEquals(Response.Status.OK, response.getStatusInfo().toEnum());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void publishEventAccountNotActive() {
        CompletableFuture<Response> future = new CompletableFuture<>();
        eventService.publishEvent("1", "data", future);

        try {
            Response response = future.get();
            Assert.assertNotNull(response);
            Assert.assertEquals(Response.Status.BAD_REQUEST, response.getStatusInfo().toEnum());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void publishEventNoAccount() {
        CompletableFuture<Response> future = new CompletableFuture<>();
        eventService.publishEvent("xxx", "data", future);

        try {
            Response response = future.get();
            Assert.assertNotNull(response);
            Assert.assertEquals(Response.Status.NOT_FOUND, response.getStatusInfo().toEnum());
        } catch (Exception e) {
            Assert.fail();
        }
    }

}
