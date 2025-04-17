package com.xxxyjade17.spiruraland.Attributes.Modifier;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpiruraDamageModifiers {
    private static final Map<UUID, AttributeModifier> damageModifiers = new HashMap<>();

    public static void add(UUID uuid, AttributeModifier modifier) {
        damageModifiers.put(uuid, modifier);
    }

    public static AttributeModifier get(UUID uuid) {
        return damageModifiers.get(uuid);
    }
}
