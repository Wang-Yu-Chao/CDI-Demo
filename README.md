# CDI-Demo

Demo for Debezium GSoC 2019 Idea: Reliable asynchronous CDI events through change data capture

## How to Run This Demo

- Run Mysql database sample:

```
docker run -it --rm --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=debezium -e MYSQL_USER=mysqluser -e MYSQL_PASSWORD=mysqlpw debezium/example-mysql:0.9
```

- Open mysql CLI:

```
docker run -it --rm --name mysqlterm --link mysql --rm mysql:5.7 sh -c 'exec mysql -h"$MYSQL_PORT_3306_TCP_ADDR" -P"$MYSQL_PORT_3306_TCP_PORT" -p"$MYSQL_ENV_MYSQL_ROOT_PASSWORD"'
```

- Use thorntail to deploy and start the demo:

```
mvn thorntail:run
```

- Configure the Embedded Engine via REST API and start the JMS producer implicitly:

```
curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:8080/connector/config -d '{ "name": "inventory-connector", "config": { "connector.class": "io.debezium.connector.mysql.MySqlConnector", "database.hostname": "localhost", "database.port": "3306", "database.user": "debezium", "database.password": "dbz", "database.server.id": "184054", "database.server.name": "dbserver1", "database.whitelist": "inventory", "offset.storage": "org.apache.kafka.connect.storage.MemoryOffsetBackingStore", "database.history": "io.debezium.relational.history.MemoryDatabaseHistory", "offset.flush.interval.ms": "100", "schemas.enable": "false"} }'```
```

- Start JMS consumers and read messages:

```
curl -i -X GET -H "Accept:application/json" http://localhost:8080/consumer
```

## Design

- Demo code are all under the `io.debezium.gsoc` package. The whole Debezium Embedded and part of Kafka runtime are contained 
in this application since compatibility issue. There are some Kafka REST APIs in Kafka runtime artifact which is conflict with 
the ones of this demo.

- Debezium Embedded Engine could be configurated through REST APIs. `io.debezium.gsoc.rest.ConnectorEndpoint` defines 
`GET` and `POST` methods for Embedded Engine configuration. `io.debezium.gsoc.rest.ConsumerEndpoint` defines `GET` method
for obtain change events.

- `io.debezium.gsoc.service.ConnectorService` class create the Embedded Engine and its change consumer only fire events 
synchronously (via RecordEventBean).

- Events are defined in the `io.debezium.gsoc.event.RecordEvent` class, which is just a wrapper for `SourceRecord`.

- `io.debezium.gsoc.handler.JMSQueueHandler` and `io.debezium.gsoc.handler.JMSTopicHandler` plays the role of asynchronous 
event handlers. They send records to JMS Queue/Topic which is injected by the Java EE container.
 
- On the consumer side, `io.debeizum.gsoc.service.QueueConsumerMDB` and `io.debezium.gsoc.service.Topic.ConsumerMDB` are
 message-driven beans that asynchronously receive messages from JMS Queue/Topic. They receive and pass messages to the 
 `io.debezium.gsoc.service.ConsumerService`. Messages are stored in a queue and was polled through REST APIs.
 
- Annotations defined in `io.debezium.gsoc.annotation` package are qualifiers for classifying events.

## Problems and Uncompleted Part

1. JMS does not work properly right now because of MDB not receiving messages from JMS Topic/Queue. This is probably the 
problem caused by asynchronous handlers as they are running on separated threads and the JMSContext they are injected are 
different from the one used by MDBs on other threads.

2. Event storage, like a database which stores events.

3. Housekeeping process.