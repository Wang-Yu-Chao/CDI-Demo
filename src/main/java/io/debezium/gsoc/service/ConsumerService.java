package io.debezium.gsoc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@ApplicationScoped
public class ConsumerService {

    Logger logger = LoggerFactory.getLogger(getClass());

    private Queue<String> messageQueue;

    public ConsumerService() {
        messageQueue = new LinkedList<>();
    }

    public List<String> getRecords() {
        List<String> list = new ArrayList<>();

        while (!messageQueue.isEmpty()) {
            list.add(messageQueue.poll());
        }

        return list;
    }

    public void addRecord(String record) {
        messageQueue.add(record);
    }
}
