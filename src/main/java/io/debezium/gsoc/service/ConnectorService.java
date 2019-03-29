package io.debezium.gsoc.service;

import io.debezium.config.Configuration;
import io.debezium.embedded.EmbeddedEngine;
import org.apache.kafka.connect.source.SourceRecord;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@ApplicationScoped
public class ConnectorService {

    @Inject
    RecordEventBean recordEventBean;

    public Map<String, String> getEngineConfig() {
        // Return the configuration of the running engine
        // Intentionally return null, since no corresponding method in EmbeddedEngine class
        return new HashMap<>();
    }

    // Create a Embedded Engine with configurations
    public void createEngine(Map<String, String> map) {
        // Set configurations for the embedded engine
        Configuration configuration;
        Configuration.Builder builder = Configuration.create();

        map.forEach((k, v) -> builder.with(k, v));
        configuration = builder.build();

        // Create the engine with this configuration
        EmbeddedEngine engine = EmbeddedEngine.create()
                .using(configuration)
                .notifying(fireEvents)
                .build();

        // Run the engine asynchronously ...
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(engine);

        // Stop the engine properly when this runtime shutting down ...
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                engine.stop();
            }
        });
    }

    private Consumer<SourceRecord> fireEvents = (rawRecord) -> {
        recordEventBean.fireEvent(rawRecord);
    };
}
