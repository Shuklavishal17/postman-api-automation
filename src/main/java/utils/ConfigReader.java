package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ConfigReader {

    private static JsonNode env;

    static {
        try {
            env = new ObjectMapper().readTree(new File("src/main/resources/env/QA_Env.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return env.get(key).asText();
    }
}
