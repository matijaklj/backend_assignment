# Backend program assignment

## Tracking Service
For the Tracking Service I used the [KumuluzEE](https://github.com/kumuluz/kumuluzee) framework to develop a microservice in Java. 
It uses the Kafka Producer client for sending events to Kafka topics.
The decision to create a separate topic for each accountId has enabled filtering by the CLI client
by subscribing to just selected accounts topics. Kafka doesn't support any filtering by default from the same topic.
Problems could arise from too many topics, Kafka can handle number of topics in thousands.

The Tracking service is connected to a Postgres database, that contains information about accounts.

## Pub/Sub System
For the Pub/Sub system I picked [Apache Kafka](https://kafka.apache.org/), which is a distributed event streaming platform.
Kafka offers high throughtput, scalability and high availability.

## CLI Client
The CLI Client is implemented in Java with the help of [picocli](https://picocli.info/) framework. It uses the fault tolerant
Kafka Consumer client to subscribe to events from Kafka topics. 

## Usage
I used Docker compose to set up the containers for Apache Kafka, Zookeeper, Postgres database and the Tracking Service.

First build the docker image for the Tracking service:
```bash
$ cd tracking-service
$ docker build --tag tracking-service .
```

Then run the command:
```bash
$ docker-compose up -d 
```

To run the CLI Client follow the instructions in the CLI Client README.
