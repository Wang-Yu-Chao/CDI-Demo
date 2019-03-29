package io.debezium.gsoc.spi;

import io.debezium.gsoc.annotation.Record;
import io.debezium.gsoc.event.RecordEvent;

import javax.enterprise.event.Observes;

public interface RecordHandler {

    public void handleRecordEvent(@Observes @Record RecordEvent event);
}
