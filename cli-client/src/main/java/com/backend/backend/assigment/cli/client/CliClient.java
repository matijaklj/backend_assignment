package com.backend.backend.assigment.cli.client;

import com.backend.backend.assigment.cli.client.exceptions.ClientException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import picocli.CommandLine.Option;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@CommandLine.Command(name = "cli-client", mixinStandardHelpOptions = true, version = "cli-client 1.0",
        description = "A simple fault-tolerant cli client to subscribe to events from kafka.",
        showAtFileInUsageHelp = true)
public class CliClient implements Callable<Integer> {

    @Option(names = {"-f", "--filter"}, description = "Filter events by Account Ids.")
    private List<String> filters = new ArrayList<>();

    @Option(names = {"-v", "--verbose"}, description = "Shows details about the execution of the client.")
    private boolean verbose;

    @Option(names = {"-c", "--consumer-config"}, description = "Set custom configuration for Kafka consumer. Accepts java properties file.")
    private File consumerConfig;

    private Set<String> topics = new HashSet();

    private KafkaClient kafkaClient;

    @Override
    public Integer call() throws Exception {
        this.addShutdownHook();
        try {
            if (verbose) {
                System.out.println("Starting CLI Client...");
            }

            this.kafkaClient = new KafkaClient(consumerConfig, this.verbose);

            while (true) {
                if (filters.isEmpty()) {
                    topics = kafkaClient.getTopics();
                } else {
                    // name of the topic is: "event-" + accountId
                    topics = filters.stream().map(f -> "event-" + f).collect(Collectors.toSet());
                }

                if (!topics.isEmpty()) {
                    if (verbose) System.out.println("Subscribing to topics: " + String.join(",", topics));
                    kafkaClient.getConsumer().subscribe(topics);
                    while (true) {
                        ConsumerRecords<String, String> records = kafkaClient.getConsumer().poll(Duration.ofMillis(100));
                        for (ConsumerRecord<String, String> record : records) {
                            System.out.printf("timestamp = %s, key = %s, data = %s%n", new Date(record.timestamp()), record.key(), record.value());
                        }

                        if (filters.isEmpty()) {
                            // check the kafka client for any new event topics
                            Set<String> newTopics = kafkaClient.getTopics();
                            if (newTopics.isEmpty())
                                break;
                            if (newTopics.size() != topics.size()) {
                                topics = newTopics;
                                if (verbose) System.out.println("Subscribing to topics: " + String.join(",", topics));
                                kafkaClient.getConsumer().subscribe(topics);
                            }
                        }
                    }
                }
            }
        } catch (IOException ioException) {
            System.out.println("Error reading consumer config file.");
        } catch (ClientException ex) {
            System.out.println(ex.getMessage());
        }catch (Exception ignored) { // catch consumer wakeup exception
        } finally {
            // graceful shutdown of the kafka consumer
            if (this.kafkaClient != null && this.kafkaClient.getConsumer() != null) {
                System.out.println("Closing Kafka consumer.");
                this.kafkaClient.getConsumer().close();
            }
        }
        return 0;
    }

    // addShutdownHook to catch SIGINT
    private void addShutdownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        }));
    }

    public void shutdown() {
        // sends kafka consumer a wakeup exception for graceful shutdown
        if (this.kafkaClient != null && this.kafkaClient.getConsumer() != null)
            this.kafkaClient.getConsumer().wakeup();
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new CliClient()).execute(args);
        System.exit(exitCode);
    }

    public List<String> getFilters() {
        return filters;
    }

}
