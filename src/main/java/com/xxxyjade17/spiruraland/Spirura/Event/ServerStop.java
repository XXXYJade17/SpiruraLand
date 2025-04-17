package com.xxxyjade17.spiruraland.Spirura.Event;

import com.xxxyjade17.spiruraland.Spirura.Data.Server.SpiruraSavedData;
import com.xxxyjade17.spiruraland.SpiruraLand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@Mod.EventBusSubscriber(modid = SpiruraLand.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE,value = Dist.DEDICATED_SERVER)
public class ServerStop {
    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        SpiruraSavedData.saveAllPlayersData();
    }
}
