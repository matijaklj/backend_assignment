# Tracking service

### Accounts database seed
The accounts data is located in `src/main/resources/data/accounts.sql`

## Usage

Use maven to build and run the microservice.

```bash
# run dependent services first for test to pass or use -DskipTests to skip tests
$ sudo docker-compose up -d zookeeper kafka postgres-db
$ cd tracking-service
$ mvn clean package
```

To build the docker image run the command:
```bash
$ docker build --tag tracking-service .
```

Send events to the endpoint:
```
BASE_URL/{accountId}?data={eventData}
```