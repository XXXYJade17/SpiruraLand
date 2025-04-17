package com.xxxyjade17.spiruraland.Spirura.Data.Server;

import com.xxxyjade17.spiruraland.Spirura.Capability.Spirura;
import com.xxxyjade17.spiruraland.Spirura.Handler.CapabilityHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public class SpiruraSavedData extends SavedData {
    private static final String DATA_NAME = "SpiruraSavedData";
    private static final Map<UUID, Spirura> playerData = new HashMap<>();
    private static final Map<String, UUID> playerUUID=new HashMap<>();

    public static void addUUID(Player player){
        playerUUID.put(player.getName().getString(), player.getUUID());
    }

    public static UUID getUUID(String playerName){
        return playerUUID.get(playerName);
    }

    public static SpiruraSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new Factory<>(SpiruraSavedData::new, SpiruraSavedData::load),
                DATA_NAME);
    }

    public static Spirura getPlayerData(UUID playerUUID) {
        return playerData.computeIfAbsent(playerUUID, k -> new Spirura());
    }

    public static Spirura getPlayerData(String name){
        UUID uuid = playerUUID.get(name);
        if(uuid!=null){
            return playerData.computeIfAbsent(uuid, k -> new Spirura());
        }else{
            return null;
        }
    }

    public void savePlayerData(UUID playerUUID, Spirura data) {
        playerData.put(playerUUID, data);
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        ListTag spiruraSavedData = new ListTag();
        for (Map.Entry<UUID, Spirura> entry : playerData.entrySet()) {
            CompoundTag player = new CompoundTag();
                UUID uuid = entry.getKey();
                player.putUUID("UUID", uuid);
                CompoundTag spirura = new CompoundTag();
                    entry.getValue().saveData(spirura);
                player.put("Spirura", spirura);
            spiruraSavedData.add(player);
        }
        compound.put("SpiruraSavedData", spiruraSavedData);

        ListTag uuidList = new ListTag();
        for (Map.Entry<String, UUID> entry : playerUUID.entrySet()) {
            CompoundTag uuidTag = new CompoundTag();
            uuidTag.putString("Name", entry.getKey());
            uuidTag.putUUID("UUID", entry.getValue());
            uuidList.add(uuidTag);
        }
        compound.put("UUIDList", uuidList);

        return compound;
    }

    public static SpiruraSavedData load(CompoundTag compound) {
        SpiruraSavedData savedData = new SpiruraSavedData();

        ListTag spiruraSavedData = compound.getList("SpiruraSavedData", Tag.TAG_COMPOUND);
        for (int i = 0; i < spiruraSavedData.size(); i++) {
            CompoundTag playerTag = spiruraSavedData.getCompound(i);
            UUID uuid = playerTag.getUUID("UUID");
            CompoundTag spirura = playerTag.getCompound("Spirura");
            Spirura data = new Spirura();
                data.loadData(spirura);
            savedData.playerData.put(uuid, data);
        }

        ListTag uuidList = compound.getList("UUIDList", Tag.TAG_COMPOUND);
        for (int i = 0; i < uuidList.size(); i++) {
            CompoundTag uuidTag = uuidList.getCompound(i);
                String name = uuidTag.getString("Name");
                UUID uuid = uuidTag.getUUID("UUID");
            savedData.playerUUID.put(name, uuid);
        }
        return savedData;
    }

    public static void saveAllPlayersData() {
        ServerLevel level = ServerLifecycleHooks.getCurrentServer().overworld();
        SpiruraSavedData savedData = get(level);
        for (Player player : level.players()) {
            Optional<Spirura> optionalPlayerXp =
                    Optional.ofNullable(player.getCapability(CapabilityHandler.SPIRURA_HANDLER));
            optionalPlayerXp.ifPresent(spirura -> {
                savedData.savePlayerData(player.getUUID(), spirura);
            });
        }
    }

    public static void loadAllPlayersData() {
        ServerLevel level = ServerLifecycleHooks.getCurrentServer().overworld();
        SpiruraSavedData savedData = get(level);
        for (Player player : level.players()) {
            Spirura data = savedData.getPlayerData(player.getUUID());
            Optional.ofNullable(player.getCapability(CapabilityHandler.SPIRURA_HANDLER))
                    .ifPresent(spirura -> {
                        CompoundTag playerData = new CompoundTag();
                        data.saveData(playerData);
                        spirura.loadData(playerData);
                    });
        }
    }
}
