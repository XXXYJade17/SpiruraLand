package com.xxxyjade17.spiruraland.Config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Translation {
    private static Translation INSTANCE;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static Map<String, String> translations = new HashMap<>();
    private static final String LANG = (String)Config.getInstance().getConfigInfo("language");

    public static Translation getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Translation();
        }
        return INSTANCE;
    }

    private Translation(){
        try {
            loadTranslation();
        } catch (IOException e) {
            LOGGER.error("Failed to load translation!");
        }
    }

    public Map<String, String> getTranslations(){
        return translations;
    }

    public static Component getMessage(String key) {
        String text = translations.getOrDefault(key, key);
        return Component.literal(text);
    }

    public static Component getMessage(String key, Object... args) {
        String text = translations.getOrDefault(key, key);
        return Component.literal(String.format(text, args));
    }

    private void loadTranslation() throws IOException{
        Path englistPath = Path.of("config/spiruraland/lang/en_us.json");
        Path chinesePath = Path.of("config/spiruraland/lang/zh_cn.json");

        Files.createDirectories(englistPath.getParent());

        loadLanguage(englistPath,"en_us.json");
        loadLanguage(chinesePath,"zh_cn.json");

        if(LANG.equals("en_us.json")){
            if(Files.exists(englistPath)){
                Reader reader = Files.newBufferedReader(englistPath, StandardCharsets.UTF_8);
                translations = new Gson().fromJson(reader, new TypeToken<Map<String, String>>(){}.getType());
            }
            else{
                loadDefaultLanguage();
                LOGGER.warn("Load default language!");
            }
        } else if (LANG.equals("zh_cn.json")) {
            if(Files.exists(chinesePath)){
                Reader reader = Files.newBufferedReader(chinesePath, StandardCharsets.UTF_8);
                translations = new Gson().fromJson(reader, new TypeToken<Map<String, String>>(){}.getType());
            }
            else{
                loadDefaultLanguage();
                LOGGER.warn("Load default language!");
            }
        }
    }

    private void loadLanguage(Path path,String fileName) throws IOException {
        if (Files.notExists(path)) {
            InputStream inputStream = Config.class.getResourceAsStream("/assets/spiruraland/lang/"+fileName);
            if (inputStream != null) {
                Files.copy(inputStream, path);
            }else{
                LOGGER.warn(fileName+" is empty!");
            }
        }
    }

    private void loadDefaultLanguage(){
        InputStream inputStream = Config.class.getResourceAsStream("/assets/spiruracore/lang/en_us.json");
        if (inputStream != null) {
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            translations = new Gson().fromJson(reader, new TypeToken<Map<String, String>>(){}.getType());
        }
    }
}
