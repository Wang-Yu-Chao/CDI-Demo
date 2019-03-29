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
import javax.jms.Queue;

@ApplicationScoped
public class JMSQueueHandler implements RecordHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String QUEUE = "java:/jms/queue/my-queue";

    @Inject
    private JMSContext context;

    @Resource(lookup = QUEUE)
    private Queue queue;

    @Override
    public void handleRecordEvent(@ObservesAsync @JMSQueue RecordEvent event) {
        logger.warn("Record event received. JMS event handler sending a record to the JMS queue");

        // Simply output the event through JMS
        context.createProducer().send(queue, event.getSourceRecord().toString());
    }
}
