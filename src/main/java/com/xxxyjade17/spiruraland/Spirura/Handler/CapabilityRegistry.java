package com.xxxyjade17.spiruraland.Spirura.Handler;

import com.xxxyjade17.spiruraland.Spirura.Capability.SpiruraProvider;
import com.xxxyjade17.spiruraland.SpiruraLand;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@Mod.EventBusSubscriber(modid = SpiruraLand.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class CapabilityRegistry {
    @SubscribeEvent
    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(CapabilityHandler.SPIRURA_HANDLER,
                EntityType.PLAYER,
                new SpiruraProvider());
    }
}
