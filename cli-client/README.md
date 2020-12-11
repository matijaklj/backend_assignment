# CLI Client

Fault tolerant CLI Client that implements the Kafka Consumer client for pooling incoming events from kafka topics.

## Usage

Use maven to build the client.

```bash
$ mvn clean package `
# use -DskipTests to skip tests
```

Run the client:
```bash
$ java -jar "target/cli-client-1.0.0-SNAPSHOT-jar-with-dependencies.jar"
```

Use the option `-h` for help.