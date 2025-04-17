package com.xxxyjade17.spiruraland.Config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AttributesConfig {
    private static AttributesConfig Instance;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson gson = new Gson();

    private static final Map<String,String> attributesUUID = new HashMap<>();
    private static final Map<String,Double> defaultValues = new HashMap<>();
    private static final Map<String,Double> maxValues = new HashMap<>();
    private static final Map<Integer,Map<Integer,Map<String, Double>>> spiruraValues = new HashMap<>();

    public static AttributesConfig getInstance() {
        if (Instance == null) {
            Instance = new AttributesConfig();
        }
        return Instance;
    }

    private AttributesConfig(){
        try{
            loadAttributesValues();
            loadAttributesUUID();
        } catch (IOException e) {
            LOGGER.error("Failed to load AttributesConfig:", e);
        }
    }

    private void loadAttributesValues() throws IOException {
        Path path = Path.of("config/spiruraland/attributes/attributesvalue.json");
        if (Files.notExists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        if (Files.notExists(path)) {
            try (InputStream inputStream = Config.class.getResourceAsStream("/" + path)) {
                if (inputStream != null) {
                    Files.copy(inputStream, path);
                } else {
                    LOGGER.warn("attributesvalue.json is empty!");
                    return;
                }
            }
        }
        try(Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
            JsonObject attributesValue = gson.fromJson(reader, JsonObject.class);
            JsonObject defaultValue = attributesValue.get("default_value").getAsJsonObject();
            JsonObject maxValue = attributesValue.get("max_value").getAsJsonObject();
            JsonObject spiruraAttributes = attributesValue.get("spirura").getAsJsonObject();
            for(Map.Entry<String, JsonElement> entry : defaultValue.entrySet()){
                defaultValues.put(entry.getKey(), entry.getValue().getAsDouble());
            }
            for(Map.Entry<String, JsonElement> entry : maxValue.entrySet()){
                maxValues.put(entry.getKey(), entry.getValue().getAsDouble());
            }
            for (Map.Entry<String, JsonElement> ranks : spiruraAttributes.entrySet()) {
                int rankNum = Integer.parseInt(ranks.getKey().substring(4));
                JsonObject rank = ranks.getValue().getAsJsonObject();
                Map<Integer, Map<String, Double>> levelAttributes = new HashMap<>();
                for (Map.Entry<String, JsonElement> levels : rank.entrySet()) {
                    int levelNum = Integer.parseInt(levels.getKey().substring(5));
                    JsonObject level = levels.getValue().getAsJsonObject();
                    Map<String, Double> attributes = new HashMap<>();
                    double damage = level.get("damage").getAsDouble();
                    double health = level.get("health").getAsDouble();
                    attributes.put("damage", damage);
                    attributes.put("health", health);
                    levelAttributes.put(levelNum, attributes);
                }
                spiruraValues.put(rankNum, levelAttributes);
            }
        }
    }

    private void loadAttributesUUID() throws IOException {
        Path path = Path.of("config/spiruraland/attributes/attributesuuid.json");
        if (Files.notExists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        if (Files.notExists(path)) {
            try (InputStream inputStream = Config.class.getResourceAsStream("/" + path)) {
                if (inputStream != null) {
                    Files.copy(inputStream, path);
                } else {
                    LOGGER.warn("attributesuuid.json is empty!");
                    return;
                }
            }
        }
        try(Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
            JsonObject UUIDs = gson.fromJson(reader, JsonObject.class);
            for(Map.Entry<String, JsonElement> entry : UUIDs.entrySet()){
                attributesUUID.put(entry.getKey(), entry.getValue().getAsString());
            }
        }
    }

    public static double getDefaultAttributeValue(String key){
        return defaultValues.get(key);
    }

    public static double getMaxAttributeValue(String key){
        return maxValues.get(key);
    }

    public static String getAttributeUUID(String key){
        return attributesUUID.get(key);
    }

    public static double getSpiruraAttributeValue(int rank, int level, String key){
        return spiruraValues.get(rank).get(level).get(key);
    }
}
