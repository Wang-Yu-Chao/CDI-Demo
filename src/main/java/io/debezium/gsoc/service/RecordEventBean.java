package io.debezium.gsoc.service;

import io.debezium.gsoc.annotation.JMSQueue;
import io.debezium.gsoc.annotation.JMSTopic;
import io.debezium.gsoc.event.RecordEvent;
import org.apache.kafka.connect.source.SourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ApplicationScoped
public class RecordEventBean implements Serializable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    @JMSQueue
    Event<RecordEvent> queueEvent;

    @Inject
    @JMSTopic
    Event<RecordEvent> topicEvent;

    public void fireEvent(SourceRecord record) {
        RecordEvent recordPayload = new RecordEvent(record);

        // Fire the record event asynchronously
        queueEvent.fireAsync(recordPayload);
        topicEvent.fireAsync(recordPayload);

        logger.warn("A record event has been fired to an JMS handler");
    }
}
