package io.debezium.gsoc.service;

import io.debezium.gsoc.handler.JMSQueueHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.*;

@MessageDriven(name = "QueueConsumerMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = JMSQueueHandler.QUEUE),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class QueueConsumerMDB implements MessageListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    ConsumerService service;

    @Override
    public void onMessage(Message message) {
        logger.warn("MDB receiving a message from the JMS queue");

        try {
            service.addRecord("From Queue: " + ((TextMessage) message).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
