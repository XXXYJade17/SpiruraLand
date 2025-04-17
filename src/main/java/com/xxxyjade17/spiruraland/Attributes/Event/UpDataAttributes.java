package com.xxxyjade17.spiruraland.Attributes.Event;

import com.xxxyjade17.spiruraland.Attributes.Modifier.ModifierHandler;
import com.xxxyjade17.spiruraland.Config.AttributesConfig;
import com.xxxyjade17.spiruraland.Spirura.Capability.Spirura;
import com.xxxyjade17.spiruraland.Spirura.Data.Server.SpiruraSavedData;
import com.xxxyjade17.spiruraland.Spirura.Handler.CapabilityHandler;
import com.xxxyjade17.spiruraland.SpiruraLand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;

import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = SpiruraLand.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE,value = Dist.DEDICATED_SERVER)
public class UpDataAttributes {
    private static final AttributesConfig attributesConfig = AttributesConfig.getInstance();
    private static int rank =0;
    private static int level =0;

    @SubscribeEvent
    public static void upDataAttributes(TickEvent.PlayerTickEvent event){
        if(event.player instanceof ServerPlayer player){
            Spirura playerSpirura = SpiruraSavedData.getPlayerData(player.getUUID());
            rank = playerSpirura.getRank();
            level = playerSpirura.getLevel();
            Optional<Spirura> optionalSpirura = Optional.ofNullable(player.getCapability(CapabilityHandler.SPIRURA_HANDLER));
            optionalSpirura.ifPresent(spirura -> {
                if(rank !=spirura.getRank()|| level !=spirura.getLevel()){
                    rank =spirura.getRank();
                    level =spirura.getLevel();
                    double damage = attributesConfig.getSpiruraAttributeValue(rank, level, "damage");
                    double health = attributesConfig.getSpiruraAttributeValue(rank, level, "health");
                    UUID spirura_damage_uuid = UUID.fromString(attributesConfig.getAttributeUUID("spirura_damage"));
                    UUID spirura_health_uuid = UUID.fromString(attributesConfig.getAttributeUUID("spirura_health"));
                    AttributeModifier damageModifier = ModifierHandler.createAdditionModifier(spirura_damage_uuid, "spirura_damage", damage);
                    AttributeModifier healthModifier = ModifierHandler.createAdditionModifier(spirura_health_uuid, "spirura_health", health);
                    AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);
                    AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
                    if (attackDamage != null) {
                        attackDamage.removePermanentModifier(spirura_damage_uuid);
                        attackDamage.addPermanentModifier(damageModifier);
                    }
                    if (maxHealth != null) {
                        maxHealth.removePermanentModifier(spirura_health_uuid);
                        maxHealth.addPermanentModifier(healthModifier);
                    }
                }
            });
        }
    }
}
