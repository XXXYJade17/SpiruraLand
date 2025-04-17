package com.xxxyjade17.spiruraland.Spirura.Event;

import com.xxxyjade17.spiruraland.Spirura.Capability.Spirura;
import com.xxxyjade17.spiruraland.Spirura.Data.Server.SpiruraSavedData;
import com.xxxyjade17.spiruraland.Spirura.Handler.CapabilityHandler;
import com.xxxyjade17.spiruraland.SpiruraLand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = SpiruraLand.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE,value = Dist.DEDICATED_SERVER)
public class LeaveAndSaveSpirura {
    @SubscribeEvent
    public static void leaveAndSaveSpiruraEvent(EntityLeaveLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayer player) {
                Optional<Spirura> optionalCE =
                        Optional.ofNullable(player.getCapability(CapabilityHandler.SPIRURA_HANDLER));
                optionalCE.ifPresent(spirura -> {
                    SpiruraSavedData savedData = SpiruraSavedData.get((ServerLevel) event.getLevel());
                    savedData.savePlayerData(player.getUUID(), spirura);
                });
            }
        }
    }
}
