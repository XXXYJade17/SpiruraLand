package com.xxxyjade17.spiruraland.Attributes.Event;

import com.xxxyjade17.spiruraland.Attributes.Modifier.SpiruraDamageModifiers;
import com.xxxyjade17.spiruraland.Attributes.Modifier.SpiruraHealthModifiers;
import com.xxxyjade17.spiruraland.Config.AttributesConfig;
import com.xxxyjade17.spiruraland.SpiruraLand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = SpiruraLand.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerDeathRespawn {
    private static final AttributesConfig attributesConfig = AttributesConfig.getInstance();
    private static final UUID spirura_damage_uuid = UUID.fromString(attributesConfig.getAttributeUUID("spirura_damage"));
    private static final UUID spirura_health_uuid = UUID.fromString(attributesConfig.getAttributeUUID("spirura_health"));
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            AttributeModifier spiruraDamage = player.getAttribute(Attributes.ATTACK_DAMAGE).getModifier(spirura_damage_uuid);
            AttributeModifier spiruraHealth = player.getAttribute(Attributes.MAX_HEALTH).getModifier(spirura_health_uuid);
            SpiruraDamageModifiers.add(player.getUUID(), spiruraDamage);
            SpiruraHealthModifiers.add(player.getUUID(), spiruraHealth);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if(event.getEntity() instanceof ServerPlayer player){
            AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if(attackDamage != null){
                AttributeModifier spiruraDamage = SpiruraDamageModifiers.get(player.getUUID());
                attackDamage.addPermanentModifier(spiruraDamage);
                attackDamage.setBaseValue(0.0D);
            }

            AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
            if(maxHealth != null){
                AttributeModifier spiruraHealth = SpiruraHealthModifiers.get(player.getUUID());
                maxHealth.addPermanentModifier(spiruraHealth);
                maxHealth.setBaseValue(0.0D);
            }
            player.teleportTo(player.getX(), player.getY(), player.getZ());
        }
    }
}
