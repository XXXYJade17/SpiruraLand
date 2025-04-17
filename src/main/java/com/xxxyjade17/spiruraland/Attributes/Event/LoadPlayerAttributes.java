package com.xxxyjade17.spiruraland.Attributes.Event;

import com.xxxyjade17.spiruraland.Attributes.Modifier.ModifierHandler;
import com.xxxyjade17.spiruraland.Attributes.Modifier.SpiruraDamageModifiers;
import com.xxxyjade17.spiruraland.Attributes.Modifier.SpiruraHealthModifiers;
import com.xxxyjade17.spiruraland.Config.AttributesConfig;
import com.xxxyjade17.spiruraland.Config.Config;
import com.xxxyjade17.spiruraland.Spirura.Capability.Spirura;
import com.xxxyjade17.spiruraland.Spirura.Handler.CapabilityHandler;
import com.xxxyjade17.spiruraland.SpiruraLand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = SpiruraLand.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LoadPlayerAttributes {
    private static final AttributesConfig attributesConfig = AttributesConfig.getInstance();

    @SubscribeEvent
    public static void loadPlayerAttributes(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Spirura spirura = player.getCapability(CapabilityHandler.SPIRURA_HANDLER);
            int rank = spirura.getRank();
            int level = spirura.getLevel();
            if(spirura != null){
                double damage = attributesConfig.getSpiruraAttributeValue(rank, level, "damage");
                double health = attributesConfig.getSpiruraAttributeValue(rank, level, "health");

                UUID spirura_damage_uuid = UUID.fromString(attributesConfig.getAttributeUUID("spirura_damage"));
                UUID spirura_health_uuid = UUID.fromString(attributesConfig.getAttributeUUID("spirura_health"));

                AttributeModifier damageModifier = ModifierHandler.createAdditionModifier(spirura_damage_uuid, "spirura_damage", damage);
                AttributeModifier healthModifier = ModifierHandler.createAdditionModifier(spirura_health_uuid, "spirura_health", health);

                AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);
                if(attackDamage !=null){
                    attackDamage.removePermanentModifier(spirura_damage_uuid);
                    attackDamage.addPermanentModifier(damageModifier);
                    SpiruraDamageModifiers.add(player.getUUID(), damageModifier);
                    attackDamage.setBaseValue(0.0D);
                }

                AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
                if(maxHealth != null){
                    maxHealth.removePermanentModifier(spirura_health_uuid);
                    maxHealth.addPermanentModifier(healthModifier);
                    SpiruraHealthModifiers.add(player.getUUID(),healthModifier);
                    maxHealth.setBaseValue(0.0D);
                }
            }
        }
    }
}
