* Backend program assigment

build docker image for tracking service:
```bash
cd tracking-service
docker build --tag tracking-service .
```

run docker compose:
```bash
cd ..
docker-compose up -d 
```


The decision to send events from each account to a separate topis was to enable filtering by the client
by subscribing to just selected accounts event. Kafka doesn't support any filtering by default from the same topic.
Problems could arise from too many topics, Kafka can handle number of topics in thousands. 