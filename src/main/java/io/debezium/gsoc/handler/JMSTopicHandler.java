package io.debezium.gsoc.handler;

import io.debezium.gsoc.annotation.JMSQueue;
import io.debezium.gsoc.event.RecordEvent;
import io.debezium.gsoc.spi.RecordHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Topic;

@ApplicationScoped
public class JMSTopicHandler implements RecordHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String TOPIC = "java:/jms/topic/my-topic";

    @Inject
    private JMSContext context;

    @Resource(lookup = TOPIC)
    private Topic topic;

    @Override
    public void handleRecordEvent(@ObservesAsync @JMSQueue RecordEvent event) {
        logger.warn("Record event received. JMS event handler sending a record to the JMS topic");

        // Simply output the event through JMS
        context.createProducer().send(topic, event.getSourceRecord().toString());
    }
}
