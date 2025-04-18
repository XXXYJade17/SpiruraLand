package com.xxxyjade17.spiruraland.Monsters.Registry;

import com.xxxyjade17.spiruraland.Monsters.Monster.FirstLevel.FirstMonster;
import com.xxxyjade17.spiruraland.SpiruraLand;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MonstersRegistries {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, SpiruraLand.MODID);

    public static final Supplier<EntityType<FirstMonster>> FIRST_MONSTER =
            ENTITY_TYPES.register("first_monster", () ->EntityType.Builder.of(FirstMonster::new, MobCategory.MISC)
                            .sized(1.0f, 2.0f)
                            .build("first_monster"));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
