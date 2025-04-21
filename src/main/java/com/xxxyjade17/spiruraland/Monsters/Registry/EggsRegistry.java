package com.xxxyjade17.spiruraland.Monsters.Registry;

import com.xxxyjade17.spiruraland.SpiruraLand;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EggsRegistry {
    public static final DeferredRegister<Item> EGGS =
            DeferredRegister.create(Registries.ITEM, SpiruraLand.MODID);

    public static final Supplier<Item> FIRST_MONSTER_SPAWN_EGG = EGGS.register("respawning_monster_spawn_egg",
            () -> new DeferredSpawnEggItem(MonstersRegistries.FIRST_MONSTER, // 对应的实体类型 Supplier
                    0x44AA66, // 主颜色 (十六进制)
                    0xAA4433, // 副颜色 (十六进制)
                    new Item.Properties())); // 物品属性

    public static void register(IEventBus eventBus) {
        EGGS.register(eventBus);
    }
}
