package com.backend.backend.assigment.cli.client;

import com.backend.backend.assigment.cli.client.exceptions.ClientException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class KafkaClient {

    private AdminClient adminClient;
    private KafkaConsumer<String, String> consumer;
    private boolean verbose = false;

    public KafkaClient(boolean verbose) throws IOException, ClientException {
        this(null, verbose);
    }

    public KafkaClient(File consumerConfigFile, boolean verbose) throws IOException,ClientException {
        this.verbose = verbose;

        Properties consumerProp = new Properties();
        Properties adminProp = new Properties();

        InputStream configInput;

        if (consumerConfigFile != null) {
            configInput = new FileInputStream(consumerConfigFile);

            consumerProp.load(configInput);

            String bootstrapServers = (String) consumerProp.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG);
            if (bootstrapServers == null)
                throw new ClientException("Error Consumer property missing bootstrap.servers property.");
            adminProp.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

            if (verbose) {
                Enumeration keys = consumerProp.keys();
                System.out.println("Loaded custom consumer config:");
                while (keys.hasMoreElements()) {
                    String key = (String)keys.nextElement();
                    String value = (String)consumerProp.get(key);
                    System.out.println(key + ": " + value);
                }
                System.out.println();
            }
        } else {
            // default consumer config
            adminProp.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

            consumerProp.setProperty("bootstrap.servers", "localhost:9092");
            consumerProp.setProperty("group.id", "test");
            consumerProp.setProperty("enable.auto.commit", "true");
            consumerProp.setProperty("auto.commit.interval.ms", "1000");
            consumerProp.setProperty("auto.offset.reset", "earliest");
            consumerProp.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            consumerProp.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        }

        try {
            this.adminClient = AdminClient.create(adminProp);
            this.consumer = new KafkaConsumer<>(consumerProp);
        } catch (Exception e) {
            throw new ClientException("Error invalid consumer configuration: " + e.getMessage());
        }
    }

    public Set<String> getTopics() {
        Set<java.lang.String> topics = new HashSet<>();

        ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
        listTopicsOptions.listInternal(true);

        try {
            topics = adminClient.listTopics(listTopicsOptions).names().get();
            topics = topics.stream().filter(t -> t.startsWith("event-")).collect(Collectors.toSet());
        } catch (Exception e) {
            System.out.println("Error fetching topics...");
        }

        return topics;
    }

    public AdminClient getAdminClient() {
        return adminClient;
    }

    public KafkaConsumer<String, String> getConsumer() {
        return consumer;
    }
}
