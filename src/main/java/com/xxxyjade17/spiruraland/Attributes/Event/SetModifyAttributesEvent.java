package com.xxxyjade17.spiruraland.Attributes.Event;

import com.xxxyjade17.spiruraattribute.Attributes.*;
import com.xxxyjade17.spiruraattribute.SpiruraAttribute;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

@Mod.EventBusSubscriber(modid = SpiruraAttribute.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetModifyAttributesEvent {
    @SubscribeEvent
    public static void setModifyAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, AttackRange.ATTACK_RANGE.get());
        event.add(EntityType.PLAYER, CritChance.CRIT_CHANCE.get());
        event.add(EntityType.PLAYER, CritMultiplier.CRIT_MULTIPLIER.get());
        event.add(EntityType.PLAYER, Defence.DEFENCE.get());
        event.add(EntityType.PLAYER, Immunity.IMMUNITY.get());
        event.add(EntityType.PLAYER, LifeStealChance.LIFE_STEAL_CHANCE.get());
        event.add(EntityType.PLAYER, LifeStealMultiplier.LIFE_STEAL_MULTIPLIER.get());
    }
}
