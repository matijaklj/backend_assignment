package client;

import com.backend.backend.assigment.cli.client.KafkaClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

public class KafkaClientTest {


    @Test
    public void customConfigTest() {
        KafkaClient kf = null;
        try {
            kf = new KafkaClient(new File("src/test/resources/test.properties"), true);
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(kf);
    }

    @Test
    public void customConfigErrorTest() {
        Assert.assertThrows(() -> new KafkaClient(new File("src/test/resources/test-err.properties"), true));
    }

    @Test
    public void defaultClientTest() {
        KafkaClient kf = null;
        try {
            kf = new KafkaClient(false);
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(kf);
        Assert.assertNotNull(kf.getAdminClient());
        Assert.assertNotNull(kf.getConsumer());

    }
}
