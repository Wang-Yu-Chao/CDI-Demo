/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.embedded;

import org.apache.kafka.connect.errors.ConnectException;

import java.util.function.Consumer;

/**
 * An exception that is used to tell the connector to process the last source record and to then stop. When raised by
 * {@link Consumer} implementations passed to {@link EmbeddedEngine.Builder#notifying(Consumer)}, this exception should
 * only be raised after that consumer has safely processed the passed event.
 *
 * @author Randall Hauch
 */
public class StopConnectorException extends ConnectException {

    private static final long serialVersionUID = 1L;

    public StopConnectorException(String msg) {
        super(msg);
    }
}
