import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class testJsonToMap {
    public static void main(String[] args) {
        String jsonData = "{ \"name\": \"inventory-connector\", \"config\": { \"connector.class\": \"io.debezium.connector.mysql.MySqlConnector\", \"tasks.max\": \"1\", \"database.hostname\": \"mysql\", \"database.port\": \"3306\", \"database.user\": \"debezium\", \"database.password\": \"dbz\", \"database.server.id\": \"184054\", \"database.server.name\": \"dbserver1\", \"database.whitelist\": \"inventory\", \"database.history.kafka.bootstrap.servers\": \"kafka:9092\", \"database.history.kafka.topic\": \"dbhistory.inventory\" } }";

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> configMap = new HashMap<>();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonData);
            JsonNode nameNode = rootNode.path("name");
            JsonNode configNode = rootNode.path("config");
//            configMap = objectMapper.readValue(configNode.asText(), new TypeReference<HashMap<String, String>>() {});

            System.out.println("name = " + nameNode.asText());
            System.out.println("config = { " );
            configNode.fields().forEachRemaining((entry) -> { System.out.println(entry.getKey() + ": " + entry.getValue()); });

        }
        catch (IOException ie) {
            ie.printStackTrace();
        }

//        map.forEach((k, v) -> { System.out.println(k + ": " + v + "\n"); });
    }
}
