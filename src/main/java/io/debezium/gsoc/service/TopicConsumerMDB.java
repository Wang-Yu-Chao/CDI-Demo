package io.debezium.gsoc.service;

import io.debezium.gsoc.handler.JMSTopicHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.*;

@MessageDriven(name = "TopicConsumerMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = JMSTopicHandler.TOPIC),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class TopicConsumerMDB implements MessageListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    ConsumerService service;

    @Override
    public void onMessage(Message message) {
        logger.warn("MDB receiving a message from the JMS topic");

        try {
            service.addRecord("From Topic: " + ((TextMessage) message).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
