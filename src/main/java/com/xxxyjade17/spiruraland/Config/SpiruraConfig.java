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

public class SpiruraConfig {
    private static SpiruraConfig INSTANCE;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson gson = new Gson();

    private static Map<Integer,String> RankNameMap =new HashMap<>();
    private static Map<Integer, Map<Integer,Integer>> RequiredExperience =new HashMap<>();
    private static Map<Integer,Map<Integer,Integer>> IncreasedExperience =new HashMap<>();
    private static Map<Integer,Map<Integer,Boolean>> Shackle = new HashMap<>();
    private static Map<Integer,Map<Integer,Float>> BreakRate = new HashMap<>();
    private static Map<Integer,Map<Integer,Float>> RateIncrease = new HashMap<>();

    public static SpiruraConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SpiruraConfig();
        }
        return INSTANCE;
    }

    private SpiruraConfig() {
        try{
            loadExperience();
            loadShackle();
            loadRankName();
        } catch (IOException e) {
            LOGGER.error("Failed to load SpiruraConfig:",e);
        }
    }

    private void loadExperience() throws IOException {
        Path path = Path.of("config/spiruraland/spirura/experience.json");
        if (Files.notExists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        if (Files.notExists(path)) {
            try (InputStream inputStream = Config.class.getResourceAsStream("/" + path)) {
                if (inputStream != null) {
                    Files.copy(inputStream, path);
                } else {
                    LOGGER.warn("experience.json is empty!");
                    return;
                }
            }
        }

        Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);

        JsonObject experience = gson.fromJson(reader, JsonObject.class);
        for (Map.Entry<String, JsonElement> rank : experience.entrySet()) {
            int rankNum = Integer.parseInt(rank.getKey().substring(4));
            Map<Integer,Integer> requiredExperience = new HashMap<>();
            Map<Integer,Integer> increasedExperience = new HashMap<>();
            for (Map.Entry<String, JsonElement> level : rank.getValue().getAsJsonObject().entrySet()) {
                int levelNum = Integer.parseInt(level.getKey().substring(5));
                int require = level.getValue().getAsJsonObject().get("require").getAsInt();
                int increase = level.getValue().getAsJsonObject().get("increase").getAsInt();
                requiredExperience.put(levelNum,require);
                increasedExperience.put(levelNum,increase);
            }
            RequiredExperience.put(rankNum,requiredExperience);
            IncreasedExperience.put(rankNum,increasedExperience);
        }
    }

    private void loadShackle() throws IOException {
        Path path = Path.of("config/spiruraland/spirura/shackledata.json");
        if (Files.notExists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        if (Files.notExists(path)) {
            try (InputStream inputStream = Config.class.getResourceAsStream("/" + path)) {
                if (inputStream != null) {
                    Files.copy(inputStream, path);
                } else {
                    LOGGER.warn("shackledata.json is empty!");
                    return;
                }
            }
        }
        Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        JsonObject shackledata = gson.fromJson(reader, JsonObject.class);
        for (Map.Entry<String, JsonElement> rank : shackledata.entrySet()) {
            int rankNum = Integer.parseInt(rank.getKey().substring(4));
            Map<Integer,Boolean> shackle = new HashMap<>();
            Map<Integer,Float> breakRate = new HashMap<>();
            Map<Integer,Float> rateIncrease = new HashMap<>();
            for (Map.Entry<String, JsonElement> level : rank.getValue().getAsJsonObject().entrySet()) {
                int levelNum = Integer.parseInt(level.getKey().substring(5));
                boolean Shackle=level.getValue().getAsJsonObject().get("shackle").getAsBoolean();
                if(Shackle){
                    float BreakRate=level.getValue().getAsJsonObject().get("break_rate").getAsFloat();
                    float RateIncrease=level.getValue().getAsJsonObject().get("rate_increase").getAsFloat();
                    breakRate.put(levelNum,BreakRate);
                    rateIncrease.put(levelNum,RateIncrease);
                }
                shackle.put(levelNum,Shackle);
            }
            Shackle.put(rankNum,shackle);
            BreakRate.put(rankNum,breakRate);
            RateIncrease.put(rankNum,rateIncrease);
        }
    }

    private void loadRankName() throws IOException {
        Path path = Path.of("config/spiruraland/spirura/rankname.json");
        if (Files.notExists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        if (Files.notExists(path)) {
            try (InputStream inputStream = Config.class.getResourceAsStream("/" + path)) {
                if (inputStream != null) {
                    Files.copy(inputStream, path);
                } else {
                    LOGGER.warn("rankname.json is empty!");
                    return;
                }
            }
        }
        Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        JsonObject ranks = gson.fromJson(reader, JsonObject.class);
        for (Map.Entry<String, JsonElement> rank : ranks.entrySet()) {
            int rankNum = Integer.parseInt(rank.getKey().substring(4));
            String rankName = rank.getValue().getAsString();
            RankNameMap.put(rankNum,rankName);
        }
    }

    public String getRankName(int rank){
        return RankNameMap.get(rank);
    }

    public String getLevelName(int level){
        return switch (level){
            case 1 -> "第一重";
            case 2 -> "第二重";
            case 3 -> "第三重";
            case 4 -> "第四重";
            case 5 -> "第五重";
            case 6 -> "第六重";
            case 7 -> "第七重";
            case 8 -> "第八重";
            case 9 -> "第九重";
            case 10 -> "圆满";
            default -> "未知";
        };
    }

    public int getRequiredExperience(int rank,int level){
        return RequiredExperience.get(rank).get(level);
    }

    public int getIncreasedExperience(int rank,int level){
        return IncreasedExperience.get(rank).get(level);
    }

    public boolean getShackle(int rank,int level){
        return Shackle.get(rank).get(level);
    }

    public float getBreakRate(int rank,int level){
        return BreakRate.get(rank).get(level);
    }

    public float getRateIncrease(int rank,int level){
        return RateIncrease.get(rank).get(level);
    }
}
