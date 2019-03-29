package io.debezium.gsoc.event;

import org.apache.kafka.connect.source.SourceRecord;

import java.io.Serializable;

public class RecordEvent implements Serializable {

    public SourceRecord sourceRecord;

    public RecordEvent(SourceRecord sourceRecord) {
        this.sourceRecord = sourceRecord;
    }

    public SourceRecord getSourceRecord() {
        return sourceRecord;
    }

    public void setSourceRecord(SourceRecord sourceRecord) {
        this.sourceRecord = sourceRecord;
    }
}
