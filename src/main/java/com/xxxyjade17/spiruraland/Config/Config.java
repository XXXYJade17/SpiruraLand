package com.xxxyjade17.spiruraland.Config;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private static Config Config;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson gson = new Gson();

    private static int ticks_per_increase;
    private static String lang;
    private static double threshold_defence;
    private static final Map<String,String> spiruraAttributesUUID = new HashMap<>();
    private static final Map<String,Double> defaultAttributesValues = new HashMap<>();
    private static final Map<String,Double> maxAttributesValues = new HashMap<>();

    public static Config getInstance() {
        if (Config == null) {
            Config = new Config();
        }
        return Config;
    }

    public Config() {
        try {
            loadConfig();
        }catch (IOException e){
            LOGGER.error("Failed to load Config:",e);
        }
    }

    private void loadConfig() throws IOException {
        Path path = Path.of("config/spiruraland/config.json");
        Files.createDirectories(path.getParent());
        if (Files.notExists(path)) {
            try (InputStream inputStream = Config.class.getResourceAsStream("/"+path)) {
                if (inputStream != null) {
                    Files.copy(inputStream, path);
                } else {
                    LOGGER.warn("Config.json is empty!");
                    return;
                }
            }
        }
        Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        JsonObject config = gson.fromJson(reader, JsonObject.class);
        JsonObject spirura = config.get("spirura").getAsJsonObject();
        JsonObject attributes = config.get("attributes").getAsJsonObject();

        lang = config.get("lang").getAsString();
        ticks_per_increase = spirura.get("ticks_per_increase").getAsInt();
        threshold_defence = attributes.get("threshold_defence").getAsDouble();
    }

    public Object getConfigInfo(String key){
        return switch(key){
            case "language"->lang;
            case "ticks_per_increase"->ticks_per_increase;
            case "threshold_defence"->threshold_defence;
            default->null;
        };
    }
}
