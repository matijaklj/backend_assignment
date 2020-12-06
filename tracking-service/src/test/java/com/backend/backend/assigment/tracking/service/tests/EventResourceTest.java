package com.backend.backend.assigment.tracking.service.tests;

import com.backend.backend.assigment.tracking.service.AccountService;
import com.backend.backend.assigment.tracking.service.EventResource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;

@Deprecated
// todo remove
public class EventResourceTest extends Arquillian {

    @Deployment
    public static WebArchive createDeployment()
    {
        return ShrinkWrap
                .create(WebArchive.class)
                .addPackages(true, Filters.exclude(".*Test.*"),
                        EventResource.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("config.yml", "../config.yml")
                .addAsManifestResource("META-INF/data/accounts.sql", "data/accounts.sql");
    }

    @Test(dataProvider = Arquillian.ARQUILLIAN_DATA_PROVIDER)
    @RunAsClient
    public void publishEvent(
            @ArquillianResteasyResource WebTarget eventResource)
    {
        //final CompletableFuture<String> future = eventResource
        //        .publishEvent("1","test-data");

        final Response response = eventResource
                .path("v1/events").request().get();
                /*.path("/v1/events/0?data=data")
                .request(MediaType.APPLICATION_JSON)
                .post(null);

                 */

        int status = response.getStatus();

        try {
            //String res = future.get();
            Assert.assertEquals("404", Integer.toString(status));
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
