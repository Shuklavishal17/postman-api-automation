package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class ConfigReader {

    private static JsonNode env;

    static {
        try {
            InputStream is = ConfigReader.class
                    .getClassLoader()
                    .getResourceAsStream("env/QA_Env.json");

            ObjectMapper mapper = new ObjectMapper();
            env = mapper.readTree(is);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load environment file");
        }
    }

    public static String get(String key) {
        return env.get(key).asText();
    }
}
