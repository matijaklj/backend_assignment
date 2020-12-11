package client;


import com.backend.backend.assigment.cli.client.CliClient;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import picocli.CommandLine;
import picocli.CommandLine.ParseResult;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CliClientTest {

    @BeforeClass
    public void setUp() {
        // code that will be invoked when this test is instantiated
    }

    @Test
    public void helpTest() {
        CommandLine cmd = new CommandLine(new CliClient());
        String[] args = {"-h"};

        try {
            cmd.parseArgs(args);
            Assert.assertTrue(cmd.isUsageHelpRequested());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void clientFilterOptionsTest() {
        System.out.println("Fast test");
        Assert.assertEquals(1, 1);

        List<String> filters = Arrays.asList("filter1", "filter2");
        String[] args = { "-f", filters.get(0), "-f", filters.get(1) };
        CliClient client = CommandLine.populateCommand(new CliClient(), args);

        Assert.assertEquals(client.getFilters(), filters);
    }

}
